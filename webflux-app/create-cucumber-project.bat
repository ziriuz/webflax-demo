mvn archetype:generate                      \
   "-DarchetypeGroupId=io.cucumber"           \
   "-DarchetypeArtifactId=cucumber-archetype" \
   "-DarchetypeVersion=7.5.0"               \
   "-DgroupId=dev.siriuz.cucumber"                  \
   "-DartifactId=cucumber-tests"               \
   "-Dpackage=cucumber-tests"                  \
   "-Dversion=1.0.0-SNAPSHOT"                 \
   "-DinteractiveMode=false"