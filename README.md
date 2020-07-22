# Elasticsearch (= 2.4.1) HTTP Basic User Auth plugin

Elasticsearch user authentication plugin with http basic auth.
This plugin provides user authentication APIs. 

## Installation 
<pre>
bin/plugin install file://xxx/pkg/http-user-auth-plugin-1.0.zip
</pre>

## Configuration
Add following lines to plugin/http-user-auth-plugin-1.0/config.yaml:
<pre>
enable: true
name: admin
password: admin
</pre>

If you set `enable` to `true`, your Elasticsearch instance will load this plugin and make sure request auth.  
`name` is a setting for user's access.
`password` is a setting for access user's password;  
