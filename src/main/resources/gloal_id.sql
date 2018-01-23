#step1, create sn_meter and generateor procedure at two individual database nodes 
CREATE TABLE sn_meter(
  sn_name VARCHAR(50) NOT NULL COMMENT '序列号名',
  increment_by SMALLINT DEFAULT 0  NOT NULL COMMENT '自增步长',
  curr_val BIGINT DEFAULT 0 NOT NULL COMMENT '当前值',
  description VARCHAR(50) NULL COMMENT '序列号定义描述' 
);

DELIMITER $$
DROP PROCEDURE IF EXISTS sn_meter_gen$$
CREATE PROCEDURE sn_meter_gen(snName VARCHAR(50))
BEGIN
  --  start transaction to lock table sn_meter to prevent the other thread to change the curr_val
  START TRANSACTION;
   
  UPDATE sn_meter
  SET curr_val=curr_val+increment_by
  WHERE sn_name=snName;
 
  SELECT
  curr_val
  FROM sn_meter
  WHERE sn_name=snName;
   
  -- the curr_val already incremented , then release the lock
  COMMIT;
  
END$$
DELIMITER


#step2, add the sn info
#node1
insert into sn_meter(sn_name,increment_by,curr_val,description) values("ORDER_ID",2,1,'第一个id服务器生成订单id');
#node2
insert into sn_meter(sn_name,increment_by,curr_val,description) values("ORDER_ID",2,2,'第二个id服务器生成订单id');