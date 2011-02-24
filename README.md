# Welcome to the CMU Commons Project

The CMU Commons Project is an effort to consolidate and share reusable Java
software components developed by various members of the CMU community.


## Project Website

You can find project documentation and release information at the following URL:

<http://sagemintblue.github.com/cmu-commons/>


## Source Repository

The primary CMU Commons source repository is hosted on GitHib:

<https://github.com/sagemintblue/cmu-commons/>

Feel free to clone the repository and send pull requests with any patches or
enhancements you'd like to contribute to the project.


## Maven Artifacts

CMU Commons release, snapshot and third-party artifacts are hosted in Maven
repositories here on GitHub. Follow the instructions at the following URL in
order to configure Maven to make use of these repositories:

<https://github.com/sagemintblue/sagemintblue-maven-repository/>

Some required dependencies are available in other Maven repositories around the
Web, but not in Maven Central. Here is a list of further `<repository>` entries
which support these dependencies:

    <repositories>
      ...
      <repository>
        <id>jboss-public</id>
        <name>JBoss</name>
        <url>https://repository.jboss.org/nexus/content/groups/public-jboss/</url>
        <releases><enabled>true</enabled></releases>
        <snapshots><enabled>false</enabled></snapshots>
      </repository>
      <repository>
        <id>springsource</id>
        <name>SpringSource</name>
        <url>http://repository.springsource.com/maven/bundles/release/</url>
        <releases><enabled>true</enabled></releases>
        <snapshots><enabled>false</enabled></snapshots>
      </repository>
      <repository>
        <id>springsource-external</id>
        <name>SpringSource External</name>
        <url>http://repository.springsource.com/maven/bundles/external/</url>
        <releases><enabled>true</enabled></releases>
        <snapshots><enabled>false</enabled></snapshots>
      </repository>
      <repository>
        <id>protostuff</id>
        <name>Protostuff</name>
        <url>http://protostuff.googlecode.com/svn/repos/maven2/</url>
        <releases><enabled>true</enabled></releases>
        <snapshots><enabled>false</enabled></snapshots>
      </repository>
      <repository>
        <id>jgit</id>
        <name>JGit</name>
        <url>http://download.eclipse.org/jgit/maven/</url>
        <releases><enabled>true</enabled></releases>
        <snapshots><enabled>false</enabled></snapshots>
      </repository>
    </repositories>
