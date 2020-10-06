# README #

Using the OWL API to extract modules from OWL ontologies. Includes seperate class for getting all subclasses of a particular class.
Maven

### Maven ###
Build with <code>mvn package</code>

### Java arguments ###

- Extract module (TOP/BOT/STAR)
<p><code>java Xms4g -cp owl-modules.jar kik.modules/ModuleExtractor "./SNOMEDCT.owl" "./seed.txt" "TOP"</code></p>

- Get subclasses of a given class
<p><code>java Xms4g -cp owl-modules.jar kik.modules/SubclassExtractor "./SNOMEDCT.owl" "http://snomed.info/id/74732009"</code></p>


### Seed signature ###
One class per line
```
http://www.orpha.net/ORDO/Orphanet_C001
http://www.orpha.net/ORDO/Orphanet_377790
http://www.orpha.net/ORDO/Orphanet_377796
http://www.orpha.net/ORDO/Orphanet_206599
http://www.orpha.net/ORDO/Orphanet_1361
http://www.orpha.net/ORDO/Orphanet_168615
http://www.orpha.net/ORDO/Orphanet_440731
http://www.orpha.net/ORDO/Orphanet_254704
http://www.orpha.net/ORDO/Orphanet_280183
http://www.orpha.net/ORDO/Orphanet_447795
http://www.orpha.net/ORDO/Orphanet_168612
http://www.orpha.net/ORDO/Orphanet_228000
http://www.orpha.net/ORDO/Orphanet_1035
``` 
