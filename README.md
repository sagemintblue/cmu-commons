# Welcome to the CMU Commons Project

The CMU Commons Project is an effort to consolidate and share reusable Java
software components developed by various members of the CMU community.


## Project Website

You can find project documentation and release information at the following URL:

<http://sagemintblue.github.com/cmu-commons/>


## Source Repository

The primary CMU Commons source repository is hosted on GitHib:

<https://github.com/sagemintblue/cmu-commons>

Feel free to clone the repository and send pull requests with any patches or
enhancements you'd like to contribute to the project.


## Maven Artifacts

CMU Commons module snapshot, release, and third-party artifacts are hosted in a
Maven repository on GitHub:

<https://github.com/sagemintblue/sagemintblue-maven-repository/raw/master/>

To start using CMU Commons module release artifacts within your Maven project, add the following
sections to your project's `pom.xml` file:

    <project>
      ...
      <repositories>
        <repository>
          <id>sagemintblue-releases</id>
          <name>Sagemintblue Releases</name>
          <url>https://github.com/sagemintblue/sagemintblue-maven-repository/raw/master/releases</url>
          <releases><enabled>true</enabled></releases>
          <snapshots><enabled>false</enabled></snapshots>
        </repository>
      </repositories>
      ...
      <dependencies>
        <dependency>
          <groupId>edu.cmu.commons</groupId>
          <artifactId>MODULE_NAME</artifactId>
          <version>MODULE_VERSION</version>
        </dependency>
      </dependencies>
      ...
    </project>
