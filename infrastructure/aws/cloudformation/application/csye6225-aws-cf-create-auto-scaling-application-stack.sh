#!/usr/bin/env bash
#Script to create the application stack


display_usage()
{
echo "Usage:$0 [StackName] [KeyPairName] [networkStackName] [policyStackName]"
}
if [[ $# -lt 4 ]];then
	display_usage
	exit 1
fi

appStackName=$1
echo "App stack name:"$appStackName
keyPairName=$2
networkStackName=$3
policyStackName=$4

amiId=$(aws ec2 describe-images --owners self --query 'reverse(sort_by(Images,&CreationDate)[].ImageId)[0]' --output text)
echo "Selected AMI Id-> "$amiId
domain=$(aws route53 list-hosted-zones --query HostedZones[0].Name --output text)
certificateArn=$(aws acm list-certificates --query CertificateSummaryList[0].CertificateArn --output text)
aliasDomainName="nowaf.${domain}"
name=${domain::-1}
hostedZoneID=$(aws route53 list-hosted-zones --query HostedZones[0].Id --output text)
newHostedZoneID=${hostedZoneID:12:${#hostedZoneID}}
BucketName="${name}.csye6225.com"
echo ${BucketName}
StackID=$(aws cloudformation create-stack --stack-name ${appStackName} --template-body file://csye6225-cf-auto-scaling-application.json --capabilities CAPABILITY_NAMED_IAM --parameters ParameterKey=KeyPair,ParameterValue=${keyPairName} ParameterKey=ImageID,ParameterValue=${amiId} ParameterKey=S3Bucket,ParameterValue=${BucketName} ParameterKey=NetworkStackName,ParameterValue=${networkStackName} ParameterKey=certificateARN,ParameterValue=$certificateArn ParameterKey=newHostedZoneID,ParameterValue=$newHostedZoneID ParameterKey=aliasDomainName,ParameterValue=$aliasDomainName ParameterKey=PolicyStackName,ParameterValue=${policyStackName}|grep StackId)
if [[ $? -eq 0 ]]; then
echo "Stack Creation initiated"
    stackCompletion=$(aws cloudformation wait stack-create-complete --stack-name ${appStackName})
        if [[ $? -eq 0 ]]; then
            echo "Stack with name $appStackName creation completed successfully!!"
            exit 0
        else
            echo "Error creating CloudFormation Application Stack"
        fi
else
    echo "Error creating CloudFormation Application Stack"
    echo ${StackID}
    exit 1
fi
