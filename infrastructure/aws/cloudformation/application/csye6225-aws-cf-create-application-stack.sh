#!/usr/bin/env bash
#Script to create the application stack

display_usage()
{
echo "Usage:$0 [StackName] [KeyPairName]"
}
if [[ $# -lt 2 ]];then
	display_usage
	exit 1
fi

appStackName=$1
echo "App stack name:"$appStackName
keyPairName=$2

amiId=$(aws ec2 describe-images --owners self --query 'reverse(sort_by(Images,&CreationDate)[].ImageId)[0]' --output text)
echo "Selected AMI Id-> "$amiId
domain=$(aws route53 list-hosted-zones --query HostedZones[0].Name --output text)
name=${domain::-1}
BucketName="${name}.csye6225.com"
echo $BucketName
StackID=$(aws cloudformation create-stack --stack-name $appStackName --template-body file://csye6225-cf-application.json --capabilities CAPABILITY_NAMED_IAM --parameters ParameterKey=KeyPair,ParameterValue=$keyPairName ParameterKey=ImageID,ParameterValue=$amiId |grep StackId)

if [[ -z "$StackID" ]];then
	echo "Falied to create stack $1"
	exit 1
fi

status=$(aws cloudformation describe-stacks --stack-name  $appStackName| grep StackStatus| cut -d'"' -f4)

while [[ "$status" != "CREATE_COMPLETE" ]]
do
       echo "$status"
       if [[ "$status" == "ROLLBACK_COMPLETE" ]];then
	       echo "$1 Stack_Create_Uncomplete !!"
	       exit 1
       fi
       sleep 4
       status=$(aws cloudformation describe-stacks --stack-name  $appStackName 2>&1 | grep StackStatus| cut -d'"' -f4)
done

echo "Stack with name $appStackName creation completed successfully!!"

exit 0

