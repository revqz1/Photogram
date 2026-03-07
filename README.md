# Salute

[![Build](https://github.com/viremox/Salute/actions/workflows/build.yml/badge.svg)](https://github.com/viremox/Salute/actions/workflows/build.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A Minecraft plugin that renders custom images in your server list MOTD using player head tiles. Supports both **Paper** and **Velocity**.

## How It Works

Salute splits an image into 8×8-pixel tiles, uploads each tile as a Minecraft skin via the [MineSkin](https://mineskin.org) API, and assembles the resulting player heads into a grid that is displayed in the server list MOTD.

## Requirements

- **Java 21** or newer
- **Paper 1.21+** or **Velocity 3.4+**
- A [MineSkin](https://mineskin.org) API key

## Installation

1. Download the latest release from the [Releases](https://github.com/viremox/Salute/releases) page.
2. Drop the appropriate jar into your server's `plugins/` folder:
   - `salute-paper-*.jar` for Paper servers
   - `salute-velocity-*.jar` for Velocity proxies
3. Start the server to generate the default config.
4. Add your MineSkin API key to the config file.
5. Place your images in the `plugins/Salute/images/` directory and reference them in the config.
6. Restart or run `/salute reload`.

## Building from Source

```bash
git clone https://github.com/Viremox/Salute.git
cd Salute
./gradlew build
```

Build artifacts will be in `salute-paper/build/libs/` and `salute-velocity/build/libs/`.

## Contributing

Contributions are welcome! Please read the [Contributing Guide](CONTRIBUTING.md) before submitting a pull request.

## License

This project is licensed under the [MIT License](LICENSE).
