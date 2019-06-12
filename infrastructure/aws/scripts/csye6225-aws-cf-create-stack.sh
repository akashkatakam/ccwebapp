# Bash Script to create VPCs, Subnets, Route Tables and Gateways

echo "Enter the name of the VPC-"
read vpcName

echo "VPC creation in progress..."
vName=$vpcName-cloud-vpc

VpcId=$(aws ec2 create-vpc --cidr-block 10.0.0.0/16 | grep VpcId | cut -d'"' -f4)
echo "VPC with id" $VpcId " created!"

aws ec2 create-tags --resources $VpcId --tags Key=Name,Value=$vName

echo "Subnet creation in progress..."

subnet_1=$(aws ec2 create-subnet --vpc-id $VpcId --cidr-block 10.0.0.0/24 --availability-zone us-east-1a| grep SubnetId|cut -d'"' -f4)
echo "Subnet created with "$SubnetId "!"
