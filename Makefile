#!/usr/bin/env make

PROJECT_NAME ?= magic-cv-developer-test
PROJECT_VERSION ?= 0.1.0
PROJECT_DESCRIPTION ?= Magic Tech - Data scientist assessment - Starter code

ENV ?= local
-include config/.env.${ENV}
-include config/secrets/.env.*.${ENV}
export

.DEFAULT_GOAL := help
.PHONY: help #: List available command.
help:
	@${AWK} 'BEGIN {FS = " ?#?: "; print "$(PROJECT_NAME) $(PROJECT_VERSION)\n$(PROJECT_DESCRIPTION)\n\nUsage: make \033[36m<command>\033[0m\n\nCommands:"} /^.PHONY: ?[a-zA-Z_-]/ { printf "  \033[36m%-10s\033[0m %s\n", $$2, $$3 }' $(MAKEFILE_LIST)

.PHONY: docs #: Run documentation.
docs:
	@false

.PHONY: lint #: Run linting.
lint:
	@false

.PHONY: tests #: Run tests.
tests:
	@${MVN} test

.PHONY: run #: Run backend app.
run: 
	@${MVN} -pl modules/008-vikashkothary-app -am spring-boot:run

# Run scripts using make
%:
	@if [[ -f "scripts/${*}.sh" ]]; then \
	${BASH} "scripts/${*}.sh"; else exit 1; fi

.PHONY: config
config: config/.env.${ENV}
config/.env.%:
	@cp -n config/.env.example config/.env.${ENV}

.PHONY: init #: Download project dependencies.
init:
	@${MVN} initialize

.PHONY: release #: Create a new version.
release:
	@false

.PHONY: build #: Build maven modules.
build: 
	@${MVN} compile -DskipTests

.PHONY: publish #: Publish maven modules.
publish:
	@false

.PHONY: package #: Create app package.
package:
	@${MVN} package -DskipTests=true -Dmaven.test.skip=true

.PHONY: deploy #: Deploy app packages.
deploy:
	@${MAKE} clean package heroku-deploy heroku-logs
#	@${MVN} deploy

.PHONY: clean #: Clean project build files.
clean:
	@${MVN} clean
	@[[ -z "${FORCE}" ]] || ${MVN} post-clean

.PHONY: open
open:
	@${OPEN} ${BACKEND_VIKASHKOTHARY_URL}