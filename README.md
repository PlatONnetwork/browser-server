# Platon blockchain browser server
> Provides browsing functions of the Platon chain, including functions such as blocks, transactions, verification nodes, governance parameters, tokens, etc.

## Software Architecture

- [Architecture Doc](docs/arch_doc/overall_structure.md)
- [Interface Doc](https://platonnetwork.github.io/browser-server/)

## Use technology

- gradle
- junit
- mockito
- spring,springboot
- mybatis
- logback
- client-sdk(web3j)

## Project structure

- scan-agent：Block synchronization service, which synchronizes the data of special nodes to the database of the blockchain browser.
- scan-api：Browser api service provides api interface for web pages.
- scan-common：General module.
- scan-generator：mybatis skeleton generation.
- scan-service：General Service Module
- docs：Interface documentation
- estpl：Elastic Search Template definition
- scripts：Script file
- testdata：Unit test data
- tools：development tools
- webconfig：Web configuration items


## build
### scan-agent build

```bash
gradlew clean buildTar -x test -b scan-agent/build.gradle
```

### scan-api build

```bash
gradlew clean buildTar -x test -b scan-api/build.gradle
```
## Component Version

```bash
MySQL 5.7  
redis 4.0 
elasticsearch 7.4 
apollo 1.9.1
xxljob 2.3.0
```

## About apollo startup

```bash
scan-agent,scan-api,scan-job
1-about apollo:application-apollo.yml
2-no apollo:application-platon.yml
Choose one of the above two configuration methods
```

## scan community team process
```bash
1. The repository administrator creates a feature branch (accessed based on the develop branch), feature-xxx, for the community development team.
2. The community development team forks (feature-xxx) to its own repository after the self-test on development is completed.
3. The community development team submits pr to the official feature-xxx (merge the modification of the official develop branch before submission)
4. Repository administrator merges feature-xxx into one of the proposed branch, e.g. feature-platon-1.1.5
5. Testers verify the functions of the proposed branch feature-platon-1.1.5. Issue the problem if any 
6. After the test is completed, merge it into the develop branch
```