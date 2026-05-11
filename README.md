# Wordle

## About

`wordle-java` is a Wordle clone written in Java for my computer science class.

---

## Running Wordle:

### Installer instructions:

- Head to [the latest release](https://github.com/megabyte6/wordle-java/releases/latest).
- Download the latest installer for your system.
- Run the installer.
- Have fun! 👍

### Portable image instructions:

- Head to [the latest release](https://github.com/megabyte6/wordle-java/releases/latest).
- Download the correct `.zip` for your system.
- Extract the files.
- Navigate to the `bin` folder.
- Run `wordle`.

### Nix (with flakes):

- To just run `wordle`: `nix run github:megabyte6/wordle`.
- To install `wordle` at the user-level: `nix profile install github:megabyte6/wordle`.
- To install `wordle` system-wide, you can use something akin to the following example flake:
  ```nix
  {
    description = "Example flake that includes Wordle as a package";

    inputs = {
      nixpkgs.url = "github:NixOS/nixpkgs/nixos-25.11";
      wordle.url = "github:megabyte6/wordle";
    };

    outputs = { self, nixpkgs, wordle, ... }:
    let
      system = "x86_64-linux";
    in {
      nixosConfigurations.myHost = nixpkgs.lib.nixosSystem {
      inherit system;
      modules = [
        ({ pkgs, ... }: {
          environment.systemPackages = [
            wordle.packages.${system}.default
          ];
        })
      ];
      };
    };
  }
  ```

---

## Building this project:

Note: This project should be built with JDK 25 or later.

### Build installers & executables (recommended)

1. Download this repository with the green `Code` button.
1. Check [Oracle's website](https://docs.oracle.com/en/java/javase/25/jpackage/packaging-overview.html#GUID-786E15C0-2CE7-4BDF-9B2F-AC1C57249134) for info on your system's prerequisites.
1. Optionally change the `jpackageFormat` option in `gradle.properties` if you want to build a different type of installer. By default, it will choose a format based on your current operating system. Note that you can only build for the operating system you're on (e.g. you can only build a Windows installer on Windows).
1. Run `./gradlew jpackage`
1. Check `build/jpackage` for the installer(s) and `build/jpackage/Wordle` for the executable(s).

### Build portable image with custom JRE

This option is provided in case the above option does not work. Some Linux distros like NixOS may have trouble building using jpackage.

1. Download this repository with the green `Code` button.
1. Run `./gradlew jlinkZip`
1. Check the `build` folder for the `.zip` image.
1. Play it by extracting the zip and running the `wordle` in the `bin` folder

### Nix (flakes)

For NixOS users, the nix package is recommended as the app has trouble finding the correct libraries when using the pre-built jlink image.

1. Download this repository with the green `Code` button.
1. Run `nix build`.
1. Check the `result` folder for the build image. The entry point is located at `result/bin/wordle`.

---

## Contributing

Contributions are welcome! [Create an issue](https://github.com/megabyte6/wordle-java/issues/new/choose) or open a [pull request](https://github.com/megabyte6/worlde-java/compare)! If you are interested in contributing but don't know where to start, check out the [existing issues](https://github.com/megabyte6/wordle-java/issues) for some ideas.

If you are on NixOS, you can use the dev shell to set up a development environment with all the necessary dependencies. To enter the dev shell, run `nix develop` in the project directory. This works well with `direnv` if you wish to use it. **If you plan to update any Gradle dependencies, plugins or similar, remember to delete `gradle.lock` and regenerate it with `nix run github:tadfisher/gradle2nix/v2 -- --task=jlinkZip` so Nix can correctly pre-download the correct versions.**

Note:
If editing on VSCode, run `./gradlew eclipse` to fix errors in `module-info.java` that occur due to a bug in the Java language server extension.

---

## License

This project uses the [MIT License](https://opensource.org/licenses/MIT).

---

## Thanks

This project was made possible by the following projects and people:
- [JavaFX](https://openjfx.io)
- [badass-jlink-plugin by beryx](https://github.com/beryx/badass-jlink-plugin)
- [gradle2nix](https://github.com/tadfisher/gradle2nix)
- [CachedTimelineTransition](https://github.com/fxexperience/code/blob/master/FXExperienceControls/src/com/fxexperience/javafx/animation/CachedTimelineTransition.java) and [ShakeTransition](https://github.com/fxexperience/code/blob/master/FXExperienceControls/src/com/fxexperience/javafx/animation/ShakeTransition.java) by [fxexperience](https://github.com/fxexperience)
