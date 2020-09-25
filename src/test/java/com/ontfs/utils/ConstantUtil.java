package com.ontfs.utils;

public class ConstantUtil extends TestBase {

    public static final String START_SDK = "startsdk";
    // the methodName of file
    public static final String UPLOAD_FILE = "uploadfile";
    public static final String GET_FILE_INFO = "getfileinfo";
    public static final String GET_FILE_LIST = "getfilelist";
    public static final String DOWNLOAD_FILE = "downloadfile";
    public static final String GET_FILE_READ_PLEDGE = "getfilereadpledge";
    public static final String GET_PDP_LIST = "getfilepdpinfolist";
    public static final String DELETE_FILE = "deletefiles";
    public static final String CHANGE_FILE_OWNER = "changefileowner";
    public static final String DECRYPT_FILE = "decryptfile";
    public static final String REWNEW_FILE = "renewfile";

    // the methodName of node
    public static final String GET_BALANCE = "getbalance";
    public static final String GET_NODE_LIST = "getfsnodeslist";
    public static final String QUERY_NODE = "querynode";
    public static final String DETAIL_SERVICE = "detailservice";

    // the methodName of task;
    public static final String GET_UPLOAD_TASK_INFO_BY_ID = "getuploadtaskinfobyid";
    public static final String GET_DOWNLOAD_TASK_INFO_BY_ID = "getdownloadtaskinfobyid";
    public static final String GET_ALL_UPLOAD_TASK_LIST = "getalluploadtasklist";
    public static final String GET_ALL_DOWNLOAD_TASK_LIST = "getalldownloadtasklist";
    public static final String DELETE_TASK = "deletetask";

    // the methodName of challenge
    public static final String CHALLENGE = "challenge";
    public static final String GET_CHALLENGE_LIST = "getchallengelist";
    public static final String JUDGE = "judge";

    // the method of space
    public static final String CREATE_SPACE = "createspace";
    public static final String GET_SPACE_INFO = "getspaceinfo";
    public static final String DELETE_SPACE = "deletespace";
    public static final String UPDATE_SPACE = "updatespace";


    public static final String CREATE_SECTOR = "createsector";
    public static final String DELETE_SECTOR = "deletesector";
    public static final String GET_SECTOR_INFO = "getsectorinfo";
    public static final String GET_LOCAL_SECTOR = "getlocalsectorinfo";
    public static final String GET_SECTOR_INFOS_FOR_NODE = "getsectorinfosfornode";


    public static final String FILE_NAME = "wallet.dat";

    public static final String RESULT = "result";
    public static final String DESC = "desc";
    public static final String ERROR = "error";
    public static final String ONG = "ong";

    public static final String NULL = "null";
    public static final String NULL_ARRAY = "[]";

    public static final String RESTMONEY = "RestMoney";
    public static final String BLOCK_COUNT = "FileBlockCount";
    public static final String ADDRESS = "Address";
    public static final String PROGRESS = "Progress";
    public static final String TASK_ID = "TaskID";
    public static final String TASK_BASE_INFO = "TaskBaseInfo";
    public static final String FILEHASH = "FileHash";
    public static final String STATUS = "Status";
    public static final String TIME_START = "TimeStart";
    public static final String TIME_EXPIRED = "TimeExpired";
    public static final String FILE_BLOCK_COUNT = "FileBlockCount";
    public static final String FILE_DESC = "FileDesc";
    public static final String FILE_OWNER = "FileOwner";
    public static final String READ_PLANS = "ReadPlans";
    public static final String NODE_ADDR = "NodeAddr";
    public static final String HAVE_READ_BLOCK_NUM = "HaveReadBlockNum";
    public static final String STATE = "State";
    public static final String PAY_AMOUNT = "PayAmount";
    public static final String PDP_INTERVAL = "PdpInterval";
    public static final String COPY_NUMBER = "CopyNumber";
    public static final String PROFIT = "Profit";
    public static final String PLEDGE = "Pledge";
    public static final String REWARD = "Reward";
    public static final String CURRFEERATE = "CurrFeeRate";

    // desc
    public static final String SUCCESS = "SUCCESS";
    public static final String ACCOUNT_PASSWORD_WRONG = "ACCOUNT PASSWORD WRONG";
    public static final String WRONG_ENCODED_ADDRESS = "WRONG ENCODED ADDRESS";
    public static final String INVALID_ADDRESS = "INVALID ADDRESS";
    public static final String INSUFFICIENT_BALANCE = "INSUFFICIENT BALANCE";
    public static final String NO_DSP = "NO DSP";

    // error code
    // 成功
    public static final String SUCCESS_CODE = "0";
    // 无效或超时的会话
    public static final String SESSION_EXPIRED = "41001";
    // 达到服务上限
    public static final String SERVICE_CEILING = "41002";
    // 不合法的数据格式
    public static final String ILLEGAL_DATAFORMAT = "41003";
    // 无效的版本号
    public static final String INVALID_VERSION = "41004";
    // 无效的方法
    public static final String INVALID_METHOD = "42001";
    // 无效的参数
    public static final String INVALID_PARAMS = "42002";
    // 参数长度不正确
    public static final String INVALID_PARAMS_LENGTH = "42003";
    // 配置文件类型不正确
    public static final String INVALID_CONFIG_TYPE = "43001";
    // 文件类型错误
    public static final String INVALID_FILEHASH_TYPE = "43002";
    // 上传参数错误
    public static final String INVALID_UPLOAD_OPTION_TYPE = "43003";
    // 下载参数错误
    public static final String INVALID_DOWNLOAD_OPTION_TYPE = "43004";
    // 文件url类型错误
    public static final String INVALID_FILE_URL_TYPE = "43005";
    // 文件hash列表类型错误
    public static final String INVALID_FILEHASHES_TYPE = "43006";
    // 新增时间类型错误
    public static final String INVALID_ADD_TIME_TYPE = "43007";
    // 所有权拥有者类型错误
    public static final String INVALID_OWNER_TYPE = "43008";
    // 最大限制类型错误
    public static final String INVALID_LIMIT_TYPE = "43009";
    // 容量类型错误
    public static final String INVALID_VOLUME_TYPE = "43010";
    // 文件备份数量类型错误
    public static final String INVALID_COPY_NUM_TYPE = "43011";
    // 过期时间类型错误
    public static final String INVALID_TIME_EXPIRED_TYPE = "43012";
    // 内部错误
    public static final String INTERNAL_ERROR = "44001";
    // 智能合约执行错误
    public static final String INVOKE_CONTRACT_ERROR = "44002";
    // sdk 没有初始化
    public static final String SDK_NOT_STARTED = "45001";
    // sdk已经完成初始化
    public static final String SDK_ALREADY_STARTED = "45002";

    // task code
    public static final String PREPARE_UPLOAD_ERROR_CODE = "50000";
    public static final String GET_FILEINFO_FROM_DB_ERROR_CODE = "50001";
    public static final String CREATE_DOWNLOAD_FILE_FAILED_CODE = "50002";
    public static final String DOWNLOAD_BLOCK_FAILED_CODE = "50003";
    public static final String GET_FILE_STATE_ERROR_CODE = "50004";
    public static final String WRITE_FILE_DATA_FAILED_CODE = "50005";
    public static final String DECRYPT_FILE_FAILED_CODE = "50006";
    public static final String DECRYPT_WRONG_PASSWORD_CODE = "50007";

    // task status
    public static final int TASK_START = 0;
    public static final int TASK_PAUSE = 1;
    public static final int STATTASK_FINISH = 2;

    // upload progress
    public static final int UPLOAD_ADD_TASK = 2;
    public static final int UPLOAD_FS_ADD_FILE = 3;
    public static final int UPLOAD_FS_GET_PDP_HASH_DATA = 4;
    public static final int UPLOAD_CONTRACT_STORE_FILES = 5;
    public static final int UPLOAD_FILE_PRE_TRANSFER = 6;
    public static final int UPLOAD_FILE_TRANSFER_BLOACKS = 7;
    public static final int UPLOAD_WAIT_PDP_RECORDS = 8;
    public static final int UPLOAD_REGISTER_FILE_URL = 9;           // 9
    public static final int UPLOAD_DONE = 0;
    public static final int UPLOAD_ERROR = 1;

    // download progress
    public static final int DOWNLOAD_ADD_TASK = 2;
    public static final int DOWNLOAD_FS_FOUND_FILE_SERVERS = 3;
    public static final int DOWNLOAD_FS_READ_PLEDGE = 4;
    public static final int DOWNLOAD_FS_BLOCK_DOWNLOAD_OVER = 5;
    public static final int DOWNLOAD_DONE = 0;
    public static final int DOWNLOAD_ERROR = 1;

    //challenge code
    //因节点没有在规定时间内响应
    public static final int CHALLENGE_JUDGED = 0;
    //未到规定期限，但节点也还没有响应challenge
    public static final int CHALLENGE_NO_REPLY_AND_VALID = 1;
    //已到规定期限，节点还未对challenge做出响应
    public static final int CHALLENGE_REPLY_AND_EXPIRE = 2;
    //节点对challenge做出相应并且验证成功
    public static final int CHALLENGE_REPLIED_AND_SUCCESS = 3;
    //节点对challenge做出响应但验证失败
    public static final int CHALLENGE_REPLIED_BUT_VERIFY_ERROR = 4;
    //节点之前对challenge未做出响应或响应失败后，节点按期提交的pdp证明成功，表示文件依然存储正常
    public static final int CHALLENGE_FILE_PROVE_SUCCESS = 5;


    //sector cede
    public static final String INVALID_SECTOR_ID="43005";
    public static final String  EXECUTE_ERROR="44002";
    public static final String INVALID_SECTOR_SIZE="43007";
    public static final String INVALID_SECTOR_PROVE_LEVEL="43006";

    // invalid test parameters
    //test invalid fileHash
    public static Object[] invalidFileHash = {"", " ", "qweqweweqw", "#$%\"\\", 12};
    //It is used to challenge and judge interface and test the invalid node address
    public static Object[] invalidWallet = {"", " ", "花花", "Assdjkfjsldjweuirow", "#$%\"\\", wallet, 9087123};

    public static Object[] invalidPwd = {"", " ", "wrongPwd", "花花", "#$%\"\\", 12};
    //test upload file with invalid upload file path
    public static Object[] invalidFilePath = {"", " ", uploadFilePath, uploadFilePath + "/log.excel", "#$%\"\\", "花花", 1};
    public static Object[] invalidDecryptPath = {"", "/home/ont/ontfs/ontfs-test/client/invalid/invalidFile.log", decryFileDirectory, 12};//解密路径传"#$%\"\\"，直接在sdk/download下生产#$%\"\\ 文件，空格同样
    //test invalid download file path
    public static Object[] invalidOutPath = {"", "/home/ont/ontfs/ontfs-test/client/invalid/invalidFile.log", downloadFileDirectory, 999999};//下载路径传"#$%\"\\"，直接在sdk/download下生产#$%\"\\ 文件，空格同样
    //renew time
    public static Object[] invalidTime = {-1, "", " ", "ahsjdak", "#$%\"\\"};

    public static Object[] invalidExpiredTime = {-12, 0, System.currentTimeMillis() / 1000 - 1000,
            System.currentTimeMillis() / 1000 + 3600, System.currentTimeMillis(), "", "#$%\"\\", " "};// 12323.9  小数部分自动取整
    //It is used to  test create space and upload file interface
    public static Object[] invalidCopyNum = {-1, 0, 0.1, 11, "#$%\"\\", "", " "};

    public static Object[] validCopyNum = {1.1};

    public static Object[] validStorageType = {0.1, 1.1};

    public static Object[] invalidStorageType = {-1, 2, 4.5, "#$%\"\\", "", " "};

    public static Object[] invalidSpaceVolume = {-1, 0, 0.32, 255, "", " ", "#$%\"\\"};
    public static Object[] invalidFileHash_getChallengeList = {"", " ", 12};

    public static Object[] invalidExpiredTime_space = {-12, 0, 12323.9, System.currentTimeMillis() / 1000 - 1000,
            System.currentTimeMillis() / 1000 + 3600, "", "#$%\"\\", " "};

    public static Object[] invalidLimit = {"", " ", "#$%\"\\"};//负数会查到所有。小数会取整，拿到响应数量的node
    public static Object[] invalidTaskId = {"", " ", "#$%\"\\", -1, 9824898};
}
