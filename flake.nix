{
  description = "A Nix-flake-based Java development environment";

  inputs = {
    nixpkgs.url = "https://flakehub.com/f/NixOS/nixpkgs/0.1";
    gradle2nix.url = "github:tadfisher/gradle2nix/v2";
  };

  outputs = {self, ...} @ inputs: let
    javaVersion = 25;

    # JavaFX runtime dependencies
    libPathFor = pkgs:
      pkgs.lib.makeLibraryPath [
        pkgs.glib
        pkgs.libGL
        pkgs.libxtst
        pkgs.libxxf86vm
      ];

    supportedSystems = [
      "x86_64-linux"
      "aarch64-linux"
      "aarch64-darwin"
    ];
    forEachSupportedSystem = f:
      inputs.nixpkgs.lib.genAttrs supportedSystems (
        system:
          f {
            inherit system;
            pkgs = import inputs.nixpkgs {
              inherit system;
              overlays = [inputs.self.overlays.default];
            };
          }
      );
  in {
    overlays.default = final: prev: let
      jdk = prev."jdk${toString javaVersion}";
    in {
      inherit jdk;
      maven = prev.maven.override {jdk_headless = jdk;};
      gradle = prev.gradle_9.override {java = jdk;};
      lombok = prev.lombok.override {inherit jdk;};
    };

    packages = forEachSupportedSystem (
      {
        pkgs,
        system,
      }: let
        gradleProperties = builtins.readFile ./gradle.properties;
        appVersion = let
          match = builtins.match ".*appVersion[[:space:]]*=[[:space:]]*([^[:space:]]+).*" gradleProperties;
        in
          if match == null
          then "0.0"
          else builtins.elemAt match 0;
      in {
        wordle = inputs.gradle2nix.builders.${system}.buildGradlePackage {
          pname = "wordle";
          version = appVersion;
          src = ./.;

          lockFile = ./gradle.lock;
          gradle = pkgs.gradle;
          buildJdk = pkgs.jdk;

          gradleBuildFlags = ["jlinkZip"];
          nativeBuildInputs = with pkgs; [
            makeWrapper
            unzip
          ];
          installPhase = let
            libPath = libPathFor pkgs;
          in ''
            tmp="$(mktemp -d)"
            unzip -q build/wordle.zip -d "$tmp"
            if [ -d "$tmp/image" ]; then
              mkdir -p "$out"
              cp -a "$tmp/image/." "$out/"

              if [ -f "$out/bin/wordle" ]; then
                chmod +x "$out"/bin/*
                wrapProgram "$out/bin/wordle" \
                  --set JAVA_HOME "${pkgs.jdk}" \
                  --unset JAVA_TOOL_OPTIONS \
                  ${
              if pkgs.stdenv.hostPlatform.isLinux
              then "--prefix LD_LIBRARY_PATH : ${libPath}"
              else ""
            }
              fi
            else
              echo "Expected 'image/' directory not found in wordle.zip" >&2
              exit 1
            fi
          '';

          meta = with pkgs.lib; {
            description = "A Java clone of the popular game Wordle written for my computer science class";
            homepage = "https://github.com/megabyte6/wordle-java";
            license = licenses.mit;
            mainProgram = "wordle";
            platforms = supportedSystems;
          };
        };

        default = self.packages.${system}.wordle;
      }
    );

    apps = forEachSupportedSystem (
      {system, ...}: {
        wordle = {
          type = "app";
          program = "${self.packages.${system}.default}/bin/wordle";
        };

        default = self.apps.${system}.wordle;
      }
    );

    devShells = forEachSupportedSystem (
      {
        pkgs,
        system,
      }: {
        default = pkgs.mkShellNoCC {
          packages = with pkgs; [
            gcc
            gradle
            jdk
            maven
            ncurses
            patchelf
            zlib
            self.formatter.${system}
          ];

          shellHook = let
            loadLombok = "-javaagent:${pkgs.lombok}/share/java/lombok.jar";
            prev = "\${JAVA_TOOL_OPTIONS:+ $JAVA_TOOL_OPTIONS}";
            libPath = libPathFor pkgs;
          in ''
            export JAVA_TOOL_OPTIONS="${loadLombok}${prev}"
            export LD_LIBRARY_PATH="${libPath}:''${LD_LIBRARY_PATH:-}"
          '';
        };
      }
    );

    formatter = forEachSupportedSystem ({pkgs, ...}: pkgs.nixfmt);
  };
}
