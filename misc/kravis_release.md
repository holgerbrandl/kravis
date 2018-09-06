## Release Checklist

1. Increment version in `README.md`, `gradle.build`

2. Rebuild the javadoc

```bash
cd /Users/brandl/projects/kotlin/kravis
gradle clean dokka
```

3. Update [CHANGES.md](../CHANGES.md)

4. Push and wait for travis CI results

5. Do the release

```bash


export KRAVIS_HOME="/Users/brandl/projects/kotlin/kravis";

trim() { while read -r line; do echo "$line"; done; }
kravis_version=$(grep '^version' ${KRAVIS_HOME}/build.gradle | cut -f2 -d' ' | tr -d "'" | trim)

echo "new version is $kravis_version"

### Do the github release
## see https://github.com/aktau/github-release

## create tag on github 
#github-release --help

source /Users/brandl/archive/gh_token.sh
export GITHUB_TOKEN=${GH_TOKEN}
#echo $GITHUB_TOKEN

# make your tag and upload
cd ${KRAVIS_HOME}

#git tag v${kravis_version} && git push --tags
(git diff --ignore-submodules --exit-code && git tag "v${kravis_version}")  || echo "could not tag current branch"

git push --tags

# check the current tags and existing releases of the repo
# binaries are located under $GOPATH/bin
github-release info -u holgerbrandl -r krangl

# create a formal release
github-release release \
    --user holgerbrandl \
    --repo krangl \
    --tag "v${kravis_version}" \
    --name "v${kravis_version}" \
    --description "See [CHANGES.md](https://github.com/holgerbrandl/krangl/blob/master/CHANGES.md) for changes." 
#    --pre-release


########################################################################
### Build and publish the binary release to jcenter

gradle install

# careful with this one!
gradle bintrayUpload
```

For released versions check:

- https://bintray.com/holgerbrandl/mpicbg-scicomp/krangl
- https://jcenter.bintray.com/de/mpicbg/scicomp/krangl/

--

4. Increment version to *-SNAPSHOT for next release cycle

