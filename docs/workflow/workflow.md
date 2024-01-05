# Workflow

## Issues and milestones

Our team actively leverages the GitLab issue tracker, not only as a repository of issues and milestones but also as a dynamic workspace that guides our daily operations. We initiate all tasks with well-documented issues, which form the foundation of our workflow. Each milestone within the repository maps directly to a project release, offering a clear framework for what needs to be accomplished and our current status relative to the upcoming release. The milestone progress bar serves as an essential tool, providing a visual representation of the remaining workload as we approach each release.

Furthermore, we actively engage with the issue board to maintain a bird's-eye view of ongoing tasks, clearly displaying what each team member is focused on. This visualization ensures a transparent workflow where every contributor is aware of their colleagues' activities, promoting a cohesive team environment.

Each issue is meticulously detailed, accompanied by a unique identifier used in our commit messages, which streamlines the process of tracking progress and identifying remaining tasks.

The systematic use of issue labels has greatly enhanced our organizational capabilities. On the issue board, the impact of these labels is unmistakable; they immediately clarify the essence of each issue, enabling us to sort and prioritize work efficiently. The labels are thoughtfully named for intuitive recognition and swift navigation:

- Core
- Design (From Sprint 3)
- Doc
- Doing
- General
- Mr-review (From Sprint 3)
- Rest-api (From Sprint 3)
- Structure
- Testing
- Todo
- UI
- ASAP (which is a prioritized labels)

### 4. Review

- Another team member should approve the MR after reviewing the code.
- Upon approval, the MR is merged into the `Sprint2` branch.

This ensures quality control, as all changes are reviewed by at least one team member.

## Git Procedures

### 1. Issue Creation

Since Sprint 1, we have focused on clarity in issue descriptions to ensure that everyone understands what needs to be done.

- **Clarify Tasks**: For vague tasks, specify the requirements using checkboxes within the issue to break down the task into actionable items.
- **Branch Creation**: Create a local feature branch named after the issue, such as `15-write-test-to-user-in-core-module`.
- **Consistent Coding Standards**: Follow the coding standards as per .
- **Continuous Integration**: Ensure that your code integrates and passes all tests in the CI pipeline before pushing to the repository.

### 2. Commit

- **Commit Messages**: Adhere to [conventional commits](https://gist.github.com/qoomon/5dfcdf8eec66a051ecd85625518cfd13) for a clear history and easier navigation.
- **Pair programming** When pair programming is utilized, it's important to acknowledge all contributing authors within the commit message. This should be done in the footer section of the commit message using the following format: Co-authored-by: first_name last_name @<username@example.com>

### 3. Push and Merge Request (MR)

- After successful testing, push the code.
- Create an MR on GitLab, link it to the original issue, and explain what changes have been made and why.
- Require that merge requests pass the pipelines. Encourage thorough discussion and feedback in MR comments.

#### 4. MR review and merge

- **Peer Review**: Have at least one other team member review and approve the MR. This reviewer should check for code quality, adherence to project standards, and potential bugs.

- **Somethings wrong**: If the reviewer spot some issues with the MR the reviewer should either comment on the MR what is wrong, or go thought the code together.

- **Merge**: After approval, merge the MR into the correct `branch`. Never merge before all CI checks pass successfully.

This protocol ensures that all code changes are thoroughly vetted by at least one other team member, promoting quality and reliability.

By integrating these tools into our daily routine, we have cultivated a highly efficient, collaborative, and transparent work environment that not only keeps us on track but also fosters a deep sense of shared responsibility towards our collective goals.

### Communication

- Mainly through Slack, organized into specific channels for clarity.
- GitLab is utilized for discussions related to code changes and MRs.

### Pair Programming

Especially in recent sprints, we have extensively adopted pair programming as a key practice in our development process. While this approach may lengthen the time individuals dedicate to a single task, we are confident that it enhances the overall code quality.

Embracing pair programming has also facilitated comprehensive collaboration across the team, with each member contributing to every component of the project. This collaborative strategy has proven invaluable in fostering a cohesive and thorough comprehension of the project amongst all team members.
