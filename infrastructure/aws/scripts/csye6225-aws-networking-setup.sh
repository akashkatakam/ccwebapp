# Bash Script to create VPCs, Subnets, Route Tables and Gateways

RANDOM=$$
Subnet_1_availZone="us-east-1a"
Subnet_2_availZone="us-east-1b"
Subnet_3_availZone="us-east-1c"

vpc_cidr_block="10.0.0.0/20"
subnet_1_cidr_block="10.0.1.0/24"
subnet_2_cidr_block="10.0.2.0/24"
subnet_3_cidr_block="10.0.3.0/24"

echo "Enter the name of the VPC-"
read VPC_NAME
VpcName=$VPC_NAME-vpc-$RANDOM

Subnet_1_Name="Subnet-1-"$VpcName
Subnet_2_Name="Subnet-2-"$VpcName
Subnet_3_Name="Subnet-3-"$VpcName
InternetGatewayName="InternetGateway-"$VpcName

echo "VPC creation in progress..."
VpcId=$(aws ec2 create-vpc --cidr-block $vpc_cidr_block | grep VpcId | cut -d'"' -f4)
if [ -z "$VpcId" ];then
	echo "VPC creation failed"
	exit 1
fi
echo "VPC with id" $VpcId " created!"
aws ec2 create-tags --resources $VpcId --tags Key=Name,Value=$VpcName


echo "Subnet creation in progress..."
subnet_1_Id=$(aws ec2 create-subnet --vpc-id $VpcId --cidr-block $subnet_1_cidr_block --availability-zone $Subnet_1_availZone| grep SubnetId|cut -d'"' -f4)
aws ec2 create-tags --resources $subnet_1_Id --tags Key=Name,Value=$Subnet_1_Name

subnet_2_Id=$(aws ec2 create-subnet --vpc-id $VpcId --cidr-block $subnet_2_cidr_block --availability-zone $Subnet_2_availZone|grep SubnetId|cut -d'"' -f4)
aws ec2 create-tags --resources $subnet_2_Id --tags Key=Name,Value=$Subnet_2_Name

subnet_3_Id=$(aws ec2 create-subnet --vpc-id $VpcId --cidr-block $subnet_3_cidr_block --availability-zone $Subnet_3_availZone|grep SubnetId|cut -d'"' -f4)
aws ec2 create-tags --resources $subnet_3_Id --tags Key=Name,Value=$Subnet_3_Name

if [[ -z $subnet_1_Id || -z $subnet_2_Id || -z $subnet_3_Id ]];then
  echo "Failed to create subnets."
  exit 1
fi
echo "Following subnets are created: " 
echo "Subnet 1 ID: " $subnet_1_Id
echo "Subnet 2 ID: " $subnet_2_Id
echo "Subnet 3 ID: " $subnet_3_Id
	
			
echo "Internet Gateway creation in progress..."
Internate_Gateway_Id=$(aws ec2 create-internet-gateway| grep InternetGatewayId|cut -d'"' -f4)
if [ -z "$Internate_Gateway_Id" ];then
  echo "Failed to create Internet Gateway."
  exit 1
fi
echo "Internet Gateway created with id" $Internate_Gateway_Id
aws ec2 create-tags --resources $Internate_Gateway_Id --tags Key=Name,Value=$InternetGatewayName

echo "Attaching Internet Gateway to VPC..."
aws ec2 attach-internet-gateway --vpc-id $VpcId --internet-gateway-id $Internate_Gateway_Id --vpc-id $VpcId


echo "Creating RouteTable..."
RouteTableId=$(aws ec2 create-route-table --vpc-id $VpcId | grep RouteTableId | cut -d'"' -f4)
if [ -z "$RouteTableId" ];then
  echo "Failed to create Route Table."
  exit 1
fi
echo "RouteTable create with id "$RouteTableId

echo "Associating route table to each subnet..."
assc1=$(aws ec2 associate-route-table --route-table-id $RouteTableId --subnet-id $subnet_1_Id|grep AssociationId|cut -d'"' -f4)
assc2=$(aws ec2 associate-route-table --route-table-id $RouteTableId --subnet-id $subnet_2_Id|grep AssociationId|cut -d'"' -f4)
assc3=$(aws ec2 associate-route-table --route-table-id $RouteTableId --subnet-id $subnet_3_Id|grep AssociationId|cut -d'"' -f4)
if [[ -z $assc1 || -z $assc2 || -z $assc3 ]];then
  echo "Failed to create subnets association with route table."
  exit 1
fi
echo "Following are route table "$RouteTableId " association ID's for each subnet: " 
echo "Subnet 1 Association ID: " $assc1
echo "Subnet 2 Association ID: " $assc2
echo "Subnet 3 Association ID: " $assc3
				

echo "Adding a public route in the route table with destination CIDR block 0.0.0.0/0 and internet gateway as the target..."
aws ec2 create-route --route-table-id $RouteTableId --destination-cidr-block 0.0.0.0/0 --gateway-id $Internate_Gateway_Id

echo "SUCCESS"				

