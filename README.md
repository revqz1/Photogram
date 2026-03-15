# Photogram

[![Build](https://github.com/revqz/Photogram/actions/workflows/build.yml/badge.svg)](https://github.com/revqz/Photogram/actions/workflows/build.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A Minecraft plugin that renders images in your server list MOTD using player head tiles. Supports both **Paper** and **Velocity** BungyCord support coming soon.

## How It Works

Photogram splits an image into 8×8-pixel tiles, uploads each tile as a Minecraft skin via the [MineSkin](https://mineskin.org) API,
Assembls the resulting player heads into a grid that is displayed in the server list MOTD.

## Warning

Make sure the images are in multiple of 8s (height mainly) or else it wont look good.

## Requirements

- **Java 21** or newer
- A [MineSkin](https://mineskin.org) API key (Not required but will improve loading times).

## License

This project is licensed under the [MIT License](LICENSE).
