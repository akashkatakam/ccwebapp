# Bash Script to create VPCs, Subnets, Route Tables and Gateways

echo "Enter the name of the VPC-"
read VPC_NAME

VpcName=$VPC_NAME

if [ -z "$VpcName" ];then
echo "Please enter a name. Do not leave it blank"
exit 1

fi
echo "VPC creation in progress..."
VpcId=$(aws ec2 create-vpc --cidr-block 10.0.0.0/20 | grep VpcId | cut -d'"' -f4)
if [ -z "$VpcId" ];then
	echo "VPC creation failed"
	exit 1
fi
	echo "VPC with id" $VpcId " created!"

	aws ec2 create-tags --resources $VpcId --tags Key=Name,Value=$VpcName

	echo "Subnet creation in progress..."

	subnet_1_Id=$(aws ec2 create-subnet --vpc-id $VpcId --cidr-block 10.0.1.0/24 --availability-zone us-east-1a| grep SubnetId|cut -d'"' -f4)
	if [ -z "$subnet_1_Id" ];then
	echo "Subnet 1 not created"
	exit 1
	fi
		echo "Subnet 1 created with subnet id" $subnet_1_Id
		aws ec2 create-tags --resources $subnet_1_Id --tags Key=Name,Value=Subnet_1_AwsCli

		subnet_2_Id=$(aws ec2 create-subnet --vpc-id $VpcId --cidr-block 10.0.2.0/24 --availability-zone us-east-1b|grep SubnetId|cut -d'"' -f4)
		if [ -z "$subnet_2_Id" ];then
		echo "Subnet 2 not created"
		exit 1
			fi
			echo "Subnet 2 created with subnet id" $subnet_2_Id
			aws ec2 create-tags --resources $subnet_2_Id --tags Key=Name,Value=Subnet_2_AwsCli

			subnet_3_Id=$(aws ec2 create-subnet --vpc-id $VpcId --cidr-block 10.0.3.0/24 --availability-zone us-east-1c|grep SubnetId|cut -d'"' -f4)
			if [ -z "$subnet_3_Id" ];then
			echo "Subnet 3 not created"
			exit 1
				fi
				echo "Subnet 3 created with subnet id" $subnet_3_Id
				aws ec2 create-tags --resources $subnet_3_Id --tags Key=Name,Value=Subnet_3_AwsCli

				echo "Internet Gateway creation in progress..."

				Internate_Gateway_Id=$(aws ec2 create-internet-gateway| grep InternetGatewayId|cut -d'"' -f4)
				if [ -z "$Internate_Gateway_Id" ];then
				echo "Gateway not created"
				exit 1
					fi
					echo "Internet Gateway created with id" $Internate_Gateway_Id
					aws ec2 create-tags --resources $Internate_Gateway_Id --tags Key=Name,Value=InternateGateway_AwsCli

					echo "Attaching Internet Gateway to VPC..."
					aws ec2 attach-internet-gateway --vpc-id $VpcId --internet-gateway-id $Internate_Gateway_Id --vpc-id $VpcId

					echo "Creating RouteTable..."
					RouteTableId=$(aws ec2 create-route-table --vpc-id $VpcId | grep RouteTableId | cut -d'"' -f4)
					if [ -z "$RouteTableId" ];then
					echo "Route Table not created"
					exit 1
						fi
						echo "RouteTable create with id "$RouteTableId

						echo "Associating route table to each subnet..."
						assc1=$(aws ec2 associate-route-table --route-table-id $RouteTableId --subnet-id $subnet_1_Id|grep AssociationId|cut -d'"' -f4)
						if [ -z "$assc1" ];then
						echo "First association failed"
						exit 1
						fi
							echo "Association ID - "$assc1 " for associating route table "$RouteTableId" with "$subnet_1_Id

							assc2=$(aws ec2 associate-route-table --route-table-id $RouteTableId --subnet-id $subnet_2_Id|grep AssociationId|cut -d'"' -f4)
							if [ -z "$assc2" ];then
							echo "Second association failed"
							exit 1
							fi
								echo "Association ID - "$assc2 " for associating route table "$RouteTableId" with "$subnet_2_Id

								assc3=$(aws ec2 associate-route-table --route-table-id $RouteTableId --subnet-id $subnet_3_Id|grep AssociationId|cut -d'"' -f4)
								if [ -z "$assc3" ];then
								echo "Third association failed"
								exit 1
								fi
								echo "Association ID - "$assc3 " for associating route table "$RouteTableId" with "$subnet_3_Id

								echo "Adding a public route in the route table with destination CIDR block 0.0.0.0/0 and internet gateway as the target..."

								aws ec2 create-route --route-table-id $RouteTableId --destination-cidr-block 0.0.0.0/0 --gateway-id $Internate_Gateway_Id

								echo "SUCCESS"

