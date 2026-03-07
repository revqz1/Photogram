# Contributing to Salute

Thanks for your interest in contributing! Here's how to get started.

## Getting Started

1. Fork the repository and clone your fork.
2. Make sure you have **Java 21** installed.
3. Run `./gradlew build` to verify everything compiles.

## Development Setup

The project uses a multi-module Gradle layout:

- **salute-core** — platform-independent logic (pipeline, config, rendering)
- **salute-paper** — Paper plugin adapter
- **salute-velocity** — Velocity plugin adapter

Changes to shared behavior should go in `salute-core`. Platform-specific code belongs in the respective module.

## Making Changes

1. Create a feature branch from `main`: `git checkout -b feature/my-change`
2. Keep commits focused and descriptive.
3. Make sure the project builds cleanly: `./gradlew build`
4. Open a pull request against `main`.

## Pull Request Guidelines

- Describe **what** you changed and **why**.
- Reference any related issues (e.g. `Closes #42`).
- Keep PRs small and focused — one feature or fix per PR.
- Follow existing code style and conventions.

## Reporting Bugs

Use the [Bug Report](https://github.com/viremox/Salute/issues/new?template=bug_report.yml) issue template. Include:

- Server platform and version (Paper/Velocity)
- Salute version
- Steps to reproduce
- Expected vs. actual behavior

## Suggesting Features

Use the [Feature Request](https://github.com/viremox/Salute/issues/new?template=feature_request.yml) issue template.

## Code Style

- Java 21 language features are welcome.
- Use constructor injection via Guice.
- Follow the existing naming conventions in the codebase.

## License

By contributing, you agree that your contributions will be licensed under the [MIT License](LICENSE).
