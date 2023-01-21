## Release Checklist

0. Make sure to reference just public artifacts in `gradle.build`

1. Increment version in `README.md`, `gradle.build`

2. Rebuild the javadoc

```bash
cd /d/projects/misc/kravis

gradle clean dokka
```

3. Update [CHANGES.md](../CHANGES.md)

4. Push and wait for travis CI results

5. Do the release

```bash
export KRAVIS_HOME="/d/projects/misc/kravis";

trim() { while read -r line; do echo "$line"; done; }
kravis_version='v'$(grep '^version' ${KRAVIS_HOME}/build.gradle | cut -f2 -d' ' | tr -d "'" | trim)

echo "new version is $kravis_version"


if [[ $kravis_version == *"-SNAPSHOT" ]]; then
  echo "ERROR: Won't publish snapshot build $kravis_version}!" 1>&2
  exit 1
fi

cd  $KRAVIS_HOME

git status
git commit -am "${kravis_version} release"
#git diff --exit-code  || echo "There are uncomitted changes"

git tag "${kravis_version}"

git push origin 
git push origin --tags


########################################################################
### Build and publish the binary release to maven-central

#./gradlew install

# careful with this one!
# https://getstream.io/blog/publishing-libraries-to-mavencentral-2021/
# https://central.sonatype.org/pages/gradle.html
./gradlew publishToSonatype closeAndReleaseSonatypeStagingRepository


## also see https://oss.sonatype.org/
## tutorial https://getstream.io/blog/publishing-libraries-to-mavencentral-2021/
```

--

4. Increment version to *-SNAPSHOT for next release cycle

