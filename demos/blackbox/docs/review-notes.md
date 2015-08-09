# StackExchange Blackbox

## Summary
Blackbox is for storing secrets in Git, Mercurial, Subversion or Perforce. It was originally built for Puppet but that is no longer required. The basic model is that a series of scripts mediate the storage, encryption, and decryption of files. Keys are stored encrypted in SCM such that all authorized users can access them. Encryption is done via GPG, with all the pluses and minuses that comes with it.

The API is strictly command line and makes little effort to work well past the provisioning process. That means you would generally not use blackbox as a service of any kind, nor would you be likely to use it during application runtime, but for development for manual secret access, and at provisioning time with your orchestration server.

Blackbox makes no effort to solve provisioning and makes very little effort to solve problems that come with automation. That said, it is not terribly hard to layer that on top of blackbox. Blackbox also provides very little workflow support outside of standard developer flow.

## Strengths
- Can use key pairs for encryption / decryption
- Strong puppet support
- Flexible in that it isn't opinionated about automation solutions.
- Good support for multiple repo types.

## Weaknesses
- Only supports Centos/RedHat, OS-X, and Cygwin
- Doesn't have particularly compelling story for machine deployments.
- Doesn't discourage storage of secrets on the machines--leaves it up to the dev.
- Automated deploy requires a build of an RPM, or copying source. Not in repos.
- Built on top of gpg... for what that's worth
- Chef deploy encourages decryption at the puppet master, rather than the node
- No API outside of command line

## Ratings (poor / fair / good)
- Ease of setup: poor
- Easy of use: good (for base use case--more work for automation cases)
- Cloud readiness: fair
- Data center readiness: fair
- Automation / pipeline readiness: poor
- Product maturity: good
- Developer friendliness: poor
- Documentation: fair
- Stability: good (no reason to believe otherwise)

## Features
- Uses GPG for encryption
- Supports raw file, git, Mercurial, Subversion, and Perforce
- Supports workflow for easy decrypt, edit, re-encrypt cycle

## How does it attempt to solve the bootstrapping problem?
Sub-keys. Meh.

## Notes
- Seem really meant to provide encryption to puppet master. They are decrypted at that point.
- Does not encourage or help manage any particular key rotation strategy. It makes that easier by controlling access on a per-user basis based on public / private keys.
- Machine deployments: "... it permits the creation of sub-keys that have no passphrase. For automated processes, create a subkey that is only stored on the machine that needs to decrypt the files..."" - https://github.com/StackExchange/blackbox#set-up-automated-users-or-role-accounts

### Setting up automated user
- Create an account without ability to check into repository
- Create a subkey without a passphrase for deployment on box requiring decrypted content
- Add the name of the new key to the admin list
- Generate encrypted files with new key list
- Import subkey on deployment box
