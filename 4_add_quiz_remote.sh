export BUCKET=nla-cs125x-opennlp-java-backend
export TARGET_URL=https://nla-cs125x-276023.uc.r.appspot.com/

curl \
-X POST $TARGET_URL \
-H "Content-Type: application/json" \
-H  "$ARG" \
-d '{
    "quiz": {
        "id": "123",
        "name": "quiz1",
        "questions": {
            "question1": {
                "id": "456",
                "name": "question456"
            }
        }
    }
}'
