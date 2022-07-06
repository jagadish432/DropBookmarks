# HTTPS

## Command to generate a keypair using JAVA sdk

```aidl
keytool -genkeypair -keyalg RSA -dname "CN=localhost" -keystore dropbookmarks.keystore -keypass <key password> -storepass <store password>
```

* we need to provide/set the key password and store password.

## For adding new dependencies

. Add required dependencies in the pom.xml file

. Then, run mvn clean install

. Once the build is successful from the above command, then try to access the library/package specific function,variable,class, etc.. if IDE shows it then good , 
otherwise place the cursor on it and do opt+enter to search in maven dependency and add it. Also, tru the "reload from disk and rebuild the project", sometimes it also works fine.



