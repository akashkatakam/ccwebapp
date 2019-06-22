#Script to create the application stack

./csye6225-aws-cf-create-stack.sh sample

echo "Enter the application stack name:"
read stack_name

appStackName=$stack_name-app-$RANDOM
echo "App stack name:"$appStackName

echo "Printing existing VPCs IDs..."
aws ec2 describe-vpcs | grep VpcId | cut -d'"' -f4

echo "Enter one VPC ID from above..."
read vpc_id
VpcId=$vpc_id

echo "Selected VPC ID-> "$VpcId

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

echo "Printing existing S3 Buckets..."
aws s3api list-buckets | grep \"Name\"|cut -d'"' -f4
echo "Enter one S3 from above...."
read S3Bucket
s3Bucket=$S3Bucket
echo "Selected S3 Bucket Id: "$s3Bucket

echo "Print existing Domain list..."
aws route53 list-hosted-zones | grep Name | cut -d'"' -f4
echo "Enter one Domain name from above...."
read DomainName
domainName=$DomainName
echo "Selected Domain Name: "$domainName


aws cloudformation create-stack --stack-name $appStackName --template-body file://csye6225-cf-application.json --capabilities CAPABILITY_NAMED_IAM --parameters ParameterKey=VPC,ParameterValue=$VpcId ParameterKey=KeyPair,ParameterValue=$keyPairName ParameterKey=AMI,ParameterValue=$amiId ParameterKey=S3Bucket,ParameterValue=$s3Bucket ParameterKey=Domain,ParameterValue=$domainName

echo "status:"$stackStatus

