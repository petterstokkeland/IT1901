# Challenges in project

## Challenges with testing in Eclipse Che

### Challenge Description

The team encountered intermittent errors when running UI tests in Eclipse Che. The occurrence of these test failures was inconsistent and varied each time the tests were run. The main suspicion is that the view hasn't fully loaded before the testing robot begins interacting with the App. This do not occur when testing locally.

### Strategy for Resolution

To address this, explicit waiting mechanisms were implemented to ensure the UI elements were fully loaded before interaction. Additionally, adjustments were made to handle input lag, ensuring correct data entry by the testing robot. These strategies aimed to replicate the more stable conditions experienced when testing locally.

### Reflection and Learning

The process revealed the importance of accounting for environmental differences between local and containerized development environments like Eclipse Che. It highlighted the need for robust test designs that can accommodate variations in loading times and performance. The team learned about the significance of tailoring synchronization strategies and test robustness to different testing environments, and a deeper understanding of the nuances in automated UI testing.
