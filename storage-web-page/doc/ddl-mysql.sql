
    alter table c_Dictionary 
        drop 
        foreign key FKihtwvsm1cm15ofqh59qb93be ;

    drop table if exists c_Dictionary ;

    drop table if exists c_Organization ;

    drop table if exists c_OrganizationParameter ;

    drop table if exists c_Parameters ;

    drop table if exists sg_Datastream ;

    drop table if exists sg_DatastreamContent ;

    drop table if exists sg_StorageScheme ;

    create table c_Dictionary (
        id bigint not null,
        dictType varchar(64),
        dictValue varchar(64),
        hasChild tinyint,
        rank bigint,
        remark varchar(512),
        parentId bigint,
        primary key (id)
    ) comment='字典数据' ENGINE=InnoDB ;

    create table c_Organization (
        id bigint not null comment '编号',
        adminRoleId bigint comment '机构系统角色',
        adminUserId bigint,
        aliases varchar(128) comment '别名（英文）',
        isDelete bit,
        leader integer,
        name varchar(30) comment '域名称',
        organizationLevel integer,
        organizationStatus tinyint,
        parentId bigint comment '父域',
        remark varchar(128),
        safeResourceCollectionId bigint comment '安全资源库',
        primary key (id)
    ) comment='机构为多级树状结构，每一级的含义固定' ENGINE=InnoDB ;

    create table c_OrganizationParameter (
        id bigint not null,
        name varchar(128) comment '机构参数名称',
        orgId bigint comment '域id',
        vlaue varchar(512) comment '机构参数值',
        primary key (id)
    ) comment='组织机构参数由系统管理员设置，来控制机构的数据范围，比如最多允许创建100个用户等；如果没有设置，默认从系统参数中取值；' ENGINE=InnoDB ;

    create table c_Parameters (
        id bigint not null,
        name varchar(128) comment '系统参数名称',
        parameterType tinyint comment '参数类型：1. 部署参数; 2.机构参数;3.用户参数;',
        parameterValue varchar(512) comment '参数值',
        remark varchar(255) comment '备注',
        primary key (id)
    ) comment='系统参数表，存放部署参数，机构参数，用户参数' ENGINE=InnoDB ;

    create table sg_Datastream (
        id bigint not null,
        contentType varchar(31) comment '内容类型',
        extName varchar(16) comment '扩展名',
        filePath varchar(127) comment '文件相对路径',
        fileSize bigint comment '附件大小',
        lastModified bigint comment '最后修改时间',
        name varchar(63) comment '标题',
        remoteFile bit comment '是否为远程图片',
        remoteFileId bigint comment '远程文件id',
        storageId bigint comment '存储方案',
        primary key (id)
    ) comment='数据' ENGINE=InnoDB ;

    create table sg_DatastreamContent (
        id bigint not null,
        content tinyblob comment '内容',
        primary key (id)
    ) comment='数据' ENGINE=InnoDB ;

    create table sg_StorageScheme (
        id bigint not null,
        account varchar(32) comment '访问远程的用户',
        address varchar(127) comment '地址',
        defaultConfig bit comment '是否是默认配置',
        description varchar(127) comment '描述',
        localServer bit comment '是否是本地服务',
        name varchar(127) comment '策略名称',
        password varchar(32) comment '访问远程的密码',
        protocol varchar(127) comment '远程访问方法',
        remotedownurl varchar(127) comment '远端下载地址',
        remoteruleext varchar(127) comment '上传的文件后缀',
        remoterulepath varchar(127) comment '上传的文件路径',
        resourcePath varchar(64) comment '文件存储地址',
        virPath varchar(127) comment '远程路径',
        primary key (id)
    ) comment='数据' ENGINE=InnoDB ;

    alter table c_Dictionary 
        add constraint FKihtwvsm1cm15ofqh59qb93be 
        foreign key (parentId) 
        references c_Dictionary (id) ;
