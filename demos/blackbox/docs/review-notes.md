# StackExchange Blackbox

## Summary
Blackbox is for storing secrets in Git, Mercurial, Subversion or Perforce. It was originally built for Puppet but that is no longer required.

## Strengths
- Uses keypairs for encryption / decryption
- Strong puppet support

## Weaknesses
- Only supports Centos/RedHat, OS-X, and Cygwin
- Doesn't have particularly compelling story for machine deployments.
- Puppet specific: not easily chefable

## Ratings (poor / fair / good)
- Ease of setup: fair
- Easy of use:
- Cloud readiness:
- Data center readiness:
- Automation / pipeline readiness:
- Product maturity:
- Developer friendliness:
- Documentation:
- Stability:

## Features
- Uses GPG for encryption

## How does it attempt to solve the bootstrapping problem?
Subkeys.

## Notes
- Does not encourage or help manage any particular key rotation strategy. It makes that easier by controlling access on a per-user basis based on public / private keys.
- Machine deployments: "... it permits the creation of sub-keys that have no passphrase. For automated processes, create a subkey that is only stored on the machine that needs to decrypt the files..."" - https://github.com/StackExchange/blackbox#set-up-automated-users-or-role-accounts

### Setting up automated user
- Create an account without ability to check into repository
- Create a subkey without a passphrase for deployment on box requiring decrypted content
- Add the name of the new key to the admin list
- Generate encrypted files with new key list
- Import subkey on deployment box
