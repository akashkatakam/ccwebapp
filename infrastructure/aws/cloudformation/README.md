####Steps for creating CloudFormation Stack  
1. Navigate to `infrastructure/aws/cloudformation` folder and open terminal
2. Run the command `./csye6225-aws-cf-create-stack.sh [Stack_Name] [Availability_Zone_1] [Availability_Zone_2] [Availability_Zone_3]` 
3. The logs for successful creation of resources is displayed if the infrastructure is deployed. Else, the error messages are displayed
 ####Steps for Deleting CloudFormation Stack
 1. Navigate to `infrastructure/aws/cloudformation` folder and open terminal
 2. Run the command `./csye6225-aws-cf-terminate-stack.sh [Stack_Name] `