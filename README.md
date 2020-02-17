TODO
====
- [ ] http client
    - [ ] retrofit https://github.com/square/retrofit
- [ ] swagger model
- [ ] authentication
    - [ ] save token into local from json response
    - [ ] login
        - [ ] form
        - [ ] http post
    - [ ] join
        - [ ] form
        - [ ] http post
    - [ ] edit
        - [ ] form
        - [ ] http put 
    - [ ] logout
- [ ] category
    - [ ] index
- [ ] post
    - [ ] index
    - [ ] show
        - [ ] comment
            - [ ] index
            - [ ] form
                - [ ] new
                - [ ] edit
            - [ ] create
            - [ ] update
            - [ ] destroy
    - [ ] form
        - [ ] new
        - [ ] edit
    - [ ] create
    - [ ] update
    - [ ] destroy
    
    
docker-compose run --no-deps web bundle exec rake rswag
swagger-codegen generate -i http://localhost:3000/api-docs/v1/swagger.yaml -l java -o ./java
swagger-codegen generate -i http://localhost:3000/api-docs/v1/swagger.yaml -l java -o ./java -D hideGenerationTimestamp=true

java -jar modules/swagger-codegen-cli/target/swagger-codegen-cli.jar generate \
  -i http://petstore.swagger.io/v2/swagger.json \
  -l java --library=okhttp-gson \
  -D hideGenerationTimestamp=true \
  -o /var/tmp/java/okhttp-gson/ 