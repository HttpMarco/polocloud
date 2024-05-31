### Polocloud


cloud group optional properties 

### Global cluster properties

| id                | description                            | type   | default value    |
|-------------------|----------------------------------------|--------|------------------|
| prompt            | The input prefix of the cloud terminal | Text   | '&3cloud &2» &1' |
| server-port-range | The detection range of proxy ports     | Number | 25565            |
| proxy-port-range  | The detection range of server ports    | Number | 30000            |


### Cloud group properties
| id                               | description | type              | default value |
|----------------------------------|-------------|-------------------|---------------|
| static                           |             | State             | false         |
| maxOnlineServices                |             | Number            | -1            |
| startArguments                   |             | Text              | ''            |
| percentageToStartNewService      |             | Percentage Number | 100.0         |
| preferredFallback                |             | Text list         | ''            |
| startPriority                    |             | Number            | 0             |
| mergedTemplates                  |             | Text list         | []            |
| environmentVariables             |             | Text list         | []            |
| name-separator                   |             | Text              | '-'           |
| restartOnTemplateChange          |             | State             | false         |
| autoFileDeleteOnShutdown         |             | State             | false         |
| portRange                        |             | Number            | -1            |
| disablePlatformCache             |             | State             | false         |
| disableConfigurationManipulation |             | State             | false         |

### Template properties
| id               | description | type        | default value |
|------------------|-------------|-------------|---------------|
| expire           |             | Date        | -1            |
| serverPrediction |             | String list | []            |
