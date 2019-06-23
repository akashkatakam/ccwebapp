#!/usr/bin/env bash
#Script to create the application stack

./csye6225-aws-cf-create-stack.sh sample

echo "Enter the application stack name:"
read stack_name

appStackName=$stack_name-app-$RANDOM
echo "App stack name:"$appStackName

echo "Printing existing KeyPairs..."
aws ec2 describe-key-pairs | grep KeyName | cut -d'"' -f4
echo "Enter the KeyPair Name..."
read keypairname
keyPairName=$keypairname
echo "Selected key pair-> "$keyPairName

echo "Printing existing AMI Ids...."
aws ec2 describe-images --owners self|grep \"ImageId\"|cut -d'"' -f4
echo "Enter one AMI ID from above..."
read AmiId
amiId=$AmiId
echo "Selected AMI Id-> "$amiId


aws cloudformation create-stack --stack-name $appStackName --template-body file://csye6225-cf-application.json --capabilities CAPABILITY_NAMED_IAM --parameters ParameterKey=KeyPair,ParameterValue=$keyPairName ParameterKey=ImageID,ParameterValue=$amiId

echo "status:"$stackStatus

