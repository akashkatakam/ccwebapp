#!/usr/bin/env bash
#Script to create the application stack

display_usage()
{
echo "Usage:$0 [StackName]"
}
if [[ $# -lt 1 ]];then
	display_usage
	exit 1
fi

appStackName=$1-app-$RANDOM
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


stackId=$(aws cloudformation create-stack --stack-name $appStackName --template-body file://csye6225-cf-application.json --capabilities CAPABILITY_NAMED_IAM --parameters ParameterKey=KeyPair,ParameterValue=$keyPairName ParameterKey=ImageID,ParameterValue=$amiId |grep StackId)
if [[ -z "$stackID" ]];then
	echo "Falied to create stack $1"
	exit 1
fi

status=$(aws cloudformation describe-stacks --stack-name  $1| grep StackStatus| cut -d'"' -f4)

while [[ "$status" != "CREATE_COMPLETE" ]]
do
       echo "$status"
       if [[ "$status" == "ROLLBACK_COMPLETE" ]];then
	       echo "$1 Stack_Create_Uncomplete !!"
	       exit 1
       fi
       sleep 4
       status=$(aws cloudformation describe-stacks --stack-name  $1 2>&1 | grep StackStatus| cut -d'"' -f4)
done

echo "Stack with name $1 creation completed successfully!!"

exit 0

