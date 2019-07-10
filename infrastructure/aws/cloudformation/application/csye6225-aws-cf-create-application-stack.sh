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
StackID=$(aws cloudformation create-stack --stack-name $appStackName --template-body file://csye6225-cf-application.json --capabilities CAPABILITY_NAMED_IAM --parameters ParameterKey=KeyPair,ParameterValue=$keyPairName ParameterKey=ImageID,ParameterValue=$amiId ParameterKey=S3Bucket,ParameterValue=$BucketName |grep StackId)
if [[ $? -eq 0 ]]; then
echo "Stack Creation in initiated"
    stackCompletion=$(aws cloudformation wait stack-create-complete --stack-name ${appStackName})
        if [ $? -eq 0 ]; then
            echo "Stack with name $appStackName creation completed successfully!!"
            exit 0
        else
            echo "Error in creating CloudFormation Stack"
        fi
else
    echo "Error in creating CloudFormation Stack"
    echo $StackID
    exit 1
fi
