help:
	@echo make clean, compile, run, deploy
	@echo "  or"
	@echo make step0, step1, step 2
	@echo "    "
	@echo "step0 - initialize environment"
	@echo "step1 - create GCS bucket"
	@echo "step2 - copy files"
	@echo "step3 - add quiz (local)"
	@echo "step4 - add quiz (remote"
	@echo "stepz - clean up resources"

clean:
	./mvnw clean

compile:
	./mvnw compile

run:
	./mvnw -DskipTests spring-boot:run

run-shell:
	curl localhost:8080

deploy:
	./mvnw -DskipTests package appengine:deploy

step0:
	./infra/0_init.sh
step1:
	./infra/1_gsutil_makebucket.sh
step2:
	./infra/2_gsutil_copyfiles.sh
step3:
	./infra/3_add_local_quiz.sh
stepz:
	./infra/z_cleanup.sh
