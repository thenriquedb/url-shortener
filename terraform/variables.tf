variable "region" {
    description = "The AWS region to deploy resources in"
    type        = string
    default     = "us-east-1"
}

variable "vpc_cidr" {
    description = "The CIDR block for the VPC"
    type        = string
    default     =  "10.0.0.0/16"
}

variable "subnet_cidr" {
  description = "The CIDR block for the subnet"
  type        = string
  default     = "10.0.1.0/24"
}

variable "availability_zone" {
  description = "The availability zone for the subnet"
  type        = string
  default     = "us-east-1a"
}

variable "instance_type" {
    description = "The type of EC2 instance to launch"
    type        = string
    default     = "t2.micro"
}

variable "instance_public_key_path" {
    description = "The public key for the EC2 instance"
    type        = string
    default     = "~/.ssh/id_rsa.pub" # Ensure you have your public key at this path
}

variable "instance_key_pair_name" {
  description = "The name of the key pair for the EC2 instance"
  type        = string
  default     = "deployer-key"
}

variable "state_bucket" {
    description = "The name of the S3 bucket for storing Terraform state"
    type        = string
    default     = "iac-url-shortener-state-bucket"
}

