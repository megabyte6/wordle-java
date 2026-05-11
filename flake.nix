{
  description = "A Nix-flake-based Java development environment";

  inputs = {
    nixpkgs.url = "https://flakehub.com/f/NixOS/nixpkgs/0.1";
    gradle2nix.url = "github:tadfisher/gradle2nix/v2";
  };

  outputs = {self, ...} @ inputs: let
    javaVersion = 17;

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
      gradle = prev.gradle.override {java = jdk;};
      lombok = prev.lombok.override {inherit jdk;};
    };

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
