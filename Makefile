.PHONY: build delete
DOCKER_REGISTRY?=https://registry.xavi.lasalle.edu
PROJECT_NAME=feed-processor
PROJECT=##client_title##/$(PROJECT_NAME)
TAG=latest
IMAGE=$(PROJECT):$(TAG)
IMAGE_LATEST=$(DOCKER_REGISTRY)/$(PROJECT):latest

build:
	docker build -t $(IMAGE) .
	$(MAKE) sonar

push:
	docker tag $(PROJECT) $(IMAGE)
	docker push $(IMAGE_LATEST)

pull:
	docker pull $(IMAGE_LATEST)

qa:
	$(MAKE) pull
	$(MAKE) stopanddelete
	docker run -d --restart always -i --name $(PROJECT_NAME) -p 9000:9000 $(IMAGE) /root/app/bin/feed-processor -Dplay.evolutions.db.default.autoApply=true -Dconfig.resource=pre.conf

production:
	$(MAKE) pull
	$(MAKE) stopanddelete
	docker run -d --restart always -i --name $(PROJECT_NAME) -p 9000:9000 $(IMAGE)  /root/app/bin/feed-processor -Dplay.evolutions.db.default.autoApply=true -Dconfig.resource=prod.conf


stopanddelete:
	$(eval CONTAINER_ID:=$(shell docker ps -a | grep $(PROJECT_NAME) | tr -s " " | cut -d' ' -f1 | tr '\n' ' '))
	$(MAKE) stop
	[ ! -z "$(CONTAINER_ID)" ] && docker rm $(PROJECT_NAME) || echo "Container not exist"

stop:
	$(eval CONTAINER_ID:=$(shell docker ps -a | grep $(PROJECT_NAME) | tr -s " " | cut -d' ' -f1 | tr '\n' ' '))
	[ ! -z "$(CONTAINER_ID)" ] && docker update --restart=no $(CONTAINER_ID) || echo "Container not running"
	[ ! -z "$(CONTAINER_ID)" ] && docker stop $(PROJECT_NAME) || echo "Container not running"

delete:
	docker rmi -f $(PROJECT)

docker-test: docker-image
	docker tag $(IMAGE_LATEST) $(IMAGE)

docker-exec-it:
	docker run -it --rm $(IMAGE) /bin/bash

sonar:
	$(MAKE) stopanddelete
	docker run -d -i --name $(PROJECT_NAME) $(PROJECT)
	docker exec $(PROJECT_NAME) bash -c  "cd /workspace && sbt jacoco"
	docker exec $(PROJECT_NAME) bash -c "cd /workspace && sonar-scanner -Dsonar.projectKey=$(PROJECT_NAME) -Dsonar.host.url=http://sonar.xavi.lasalle.edu -Dsonar.login=5e4a625dbc76a93729116e09fa21882c5eae9e83"
	$(MAKE) stop
