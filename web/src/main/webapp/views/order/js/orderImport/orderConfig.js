//订单导入页面所有配置

var urls = {
  uploadUrl: '/MySequenceLibrary/UploadSequence',
  IsExistSequenceNameForSequenceType: '/MySequenceLibrary/IsExistSequenceNameForSequenceType'
};

var resources = {
  inputText: '上传序列文件(gbk,seq,fasta,txt)',
  noSupportText: '不支持的文件类型！',
  UI_SequenceLibrary_Create_SequenceContentInvalidChars: '序列包含非法字符：[{0}]',
  AutoModify: '自动修复',
  Sequence_Name_Invalid: '序列名称包含不合法字符 {0} .',
  UI_Customer_Validator_NotEmptyError: '不能为空',
  UI_Customer_Validator_ExistValue: '{0}已经存在,请使用其他值。',
  UI_Customer_Validator_Length: '长度必须介于{0}和{1}之间。'
};

var sngr = {
  plateRows: ["", "A", "B", "C", "D", "E", "F", "G", "H"],
  plateCols: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12],
  plateDirection: { HORIZONTAL: 1, VERTICAL: 2 },
  SampleStatus: { EMPTY: 1, FILLED: 2, VALID: 3, INVALID: 4, MARKEDEMPTY: 5 },
  containerType: { PLATE: 1, TUBE: 2, DISH: 3 }
};

//for Edit from internal
var ServiceID = '401';
var ServiceIDForPrWorking = '113';
var OrderTypeForPrWorking = '403';
var ServiceIDForSeq = '410';

var noModify = 'True';
var EditUserID = '00000000-0000-0000-0000-000000000000';
var TrackingNumber = '80-66405509';
var OrderStatus = '10';
var EditFromInternal = 'False';
var StatusOfReceived = '110';
var BusinessType = '4';
var ToTube = '切换至按管提交';
var ToPlate = '切换至按板提交';
var forPrimerWalking = 'False';
var UI_OligoPlatePreview = "预览第{0}板，共{1}板";
var UI_OligoTubesPreview = "管";
var UI_OLIGOOrder_Validation_AddRowNum = '请输入1-1000之间的正整数。';
var UI_OLIGOOrder_Validation_EnterInt = '请输入1-1000之间的正整数。';
var UI_OLIGOOrder_Validation_NumberLimit = "请输入1-1000之间的正整数。";
var UI_OLIGOOrder_Validation_SameSeq = '{0}行{1}的序列是相同的，请核对！';
var UI_OLI_ORDER_RESERVE = '金唯智测序用';
var UI_OLI_ORDER_PRIMERNAME = 'Primer名称';
var UI_OLI_ORDER_SEQ = '序列（5’to 3’）';
var UI_OLI_ORDER_SEQLENGTH = '碱基数';
var UI_OLI_ORDER_DEMANDQTY = '需求量';
var UI_OLI_ORDER_TUBE = '分装管数';
var UI_OLI_ORDER_PURIFICATION = '纯化方式';
var UI_OLI_ORDER_5MODIFICATION = "5'修饰";
var UI_OLI_ORDER_5MODIFICATION = "3'修饰";
var UI_OLI_ORDER_OTHERMODIFICATION = '中间修饰';
var UI_OLIGOOrder_GC = 'GC含量';
var UI_OLI_ORDER_WELL = "柱号";
var OLIORDER_MarkEmptyWells = '标记空孔';
var OLIORDER_Library = "每次最多选5条引物！";
var OLIORDER_LibrarySeq = "请先输入序列。";
var OLIORDER_TM = 'TM值';
var OLIORDER_Plate = "板号";
var OLIORDER_TablePrimer = '编号';
var SNGR_CONTAINER_LAYOUT_VERTICAL = "纵向提交";
var SNGR_LABELS_HORIZONTAL = "横向提交";
var Admera_OperationSuccess = "操作成功";
var MB_UI_APPLY_EMPTY_WELLS = '确认标记';
var UI_OLIGOOrder_Int = "输入值为正整数";
var UI_OLIGOOrder_Pname = "同一订单中不能存在相同的引物名称";
var UI_OLIGOOrder_Seq = '字符长度限制≦{0}nt，<br/>只识别ATGC及常用的兼并碱基代码：M=A/C   R=A/G  W=A/T   S=G/C   Y=C/T   K=G/T  V=A/G/C    H=A/C/T     D=A/G/T   B=G/C/T  N=A/G/C/T。';
var UI_OLIGOOrder_SeqBak = "勾选后，会从您订购的该引物总管数中留1管至金唯智测序部。 您将收到该引物的最终管数= 分装管数-1。";

var Warning_NoOfSequence = '您输入的条数小于实际条数，您确定要删除第{0}条之后的所有序列吗？';
var UI_OLI_NumberLimit = '不能超过Primer数目限制{0}';
var UI_Plate_NumberLimit = '板提交时，引物数必须大于等于{0}';
var UI_CANCEL = '取消';
var UI_OK = '确定';
var Olg_NPrimerS_CHANGED = '引物数已修改<br/>是否应用？';
var DP_H_1 = '我是有提交订单经验的用户。'
  , DP_H_2 = '当我进入本页面时，不需要魔法帽的操作提醒。'
  , DP_H_3 = '跳过'
  , DP_H_4 = '返回'
  , DP_H_5 = '下一步'
  , DP_H_6 = '结束';


var DataPageInfo = {
  OtherResource: {
    Inactive: '禁用',
    Active: '活跃',
    OLIORDER_SaveError: "保存出错！",
    OrderStatus_Error: "该订单状态已发生改变，不能再次修改",
    OLIORDER_SaveDraftOK: '草稿保存成功！',
    ADDSAMPLEINFO_Load: '加载中…',
    OLIORDER_NoDataInExcel: 'Excel中没有数据！',
    UI_OLIGOOrder_Upload: '上传成功！',
    UI_No_Support_File_Type: '不支持的文件类型！'

  },
  DataTableResource: {
    lengthMenu: "每页显示 _MENU_ 条",
    zeroRecords: "没有查询到记录",
    sInfo: "从 _START_ 到 _END_ 共 _TOTAL_ 条",
    sFirst: "首页",
    sPrevious: "上一页",
    sNext: "下一页",
    sLast: "尾页",
    sSearch: "搜索",
  },
  HandsonTableValidation: {
    OLIORDER_KeyIngPlateName: '请输入板名！',
    OLIORDER_InvalidMP: '无效的修饰引物',
    UI_OLIGOOrder_Validation_PrimerName: '请填写引物名称!',
    UI_OLIGOOrder_Validation_PrimerNameLength: '引物名称不能长于{0}',
    UI_OLIGOOrder_Validation_SameName: '温馨提示：{0}行{1}的引物名称不能相同，请核对！',
    UI_OLIGOOrder_SeqPAGEMinLen: '纯化方式为hPAGE时，序列长度不能少于11',
    UI_OLIGOOrder_Validation_InvalidCharacter: '序列中含有非法字符{0}，请确认！',
    UI_OLIGOOrder_Validation_Blank: '请填写序列！',
    UI_OLIGOOrder_SeqMinLen: '序列长度不能小于{0}',
    UI_OLIGOOrder_Validation_SequenceLength: '您输入的序列不能大于{0}',
    UI_OLIGOOrder_Validation_Invalid_asterisk5_Character: '格式填写错误，硫代需要在对应碱基的后面加*',
    UI_OLIGOOrder_Validation_Invalid_asterisk3_Character: '最后一个碱基无法进行硫代修饰',
    UI_OLIGOOrder_Validation_SynthesisScale: '需求量必须为正整数',
    UI_OLI_ODTubeLimit: '数字不能超过限制{0}',
    UI_OLIGOOrder_Scale_In: '需求量是必填和正整数',
    UI_OLIGOOrder_Validation_TubeQty: '分装管数必须为正整数',
    UI_OLIGOOrder_Val_TubeQtyPlate: '按板提交时，分装管数必须一致',
    UI_OLIGOOrder_Validation_Purification: '请您选择纯化方式',
    UI_OLIGOOrder_Purification_In: '您选择的纯化方式不正确',
    UI_OLIGOOrder_Validation_ModificationPho: "请在序列（5’to 3’）中填写硫代（Phosphorthioate）修饰的字符 *",
    UI_OLIGOOrder_Validation_ModificationdU: "请在序列（5’to 3’）中填写dU修饰的字符 U",
    UI_OLIGOOrder_Validation_ModificationdI: "请在序列（5’to 3’）中填写dI修饰的字符 I",
    UI_OLIGOOrder_Validation_ModificationdT: "请在序列（5’to 3’）中填写dT-Aminolinker修饰的字符 T",
    UI_OLIGOOrder_Validation_ModificationdUdI: "请在序列（5’to 3’）中填写dU+dI修饰的字符 U、I",
    UI_OLIGOOrder_Validation_ModificationdUPho: "请在序列（5’to 3’）中填写dU+Phosphorthioate修饰的字符 U、*",
    UI_OLIGOOrder_Validation_ModificationdUdTAmi: "请在序列（5’to 3’）中填写dU+dT-Aminolinker修饰的字符 U、T",
    UI_OLIGOOrder_Validation_ModificationdIPho: "请在序列（5’to 3’）中填写dI+Phosphorthioate修饰的字符 I、*",
    UI_OLIGOOrder_Validation_ModificationdIdTAmi: "请在序列（5’to 3’）中填写dI+dT-Aminolinker修饰的字符 I、T",
    UI_OLIGOOrder_Validation_ModificationdPhodTAmi: "请在序列（5’to 3’）中填写Phosphorthioate+dT-Aminolinker修饰的字符 *、T",
    UI_OLIGOOrder_SeqHPLC: "长度小于10mer的引物无法提供HPLC服务。",
    UI_OLIGOOrder_NomolTube: "2nmol及以下的需求量，无法分装。",
    UI_OLISNameSeq: "同样的序列，名字必须相同；不同的序列不能有相同的名字"

  },
  HandsonTableInfo: {
    UI_OligoInsertAbove: '插入行（上）',
    UI_OligoInsertBelow: '插入行（下）',
    UI_OligoRemoverow: '删除行',
    UI_OligoAddToLibrary: '加入到个人序列库',
    UI_OligoSelectFromLibrary: '从个人序列库中选择'
  },
  ColumnLen: {
    OLIORDER_PlateNameLen: '板名长度必须介于0和50之间。',
    OLIORDER_OrderNameLen: '订单名称长度必须介于0和50之间。',
    OLIORDER_PromotionLen: '促销码长度必须介于0和50之间。',
    OLIORDER_CommentsLen: '备注长度必须介于0和200之间。'
  }
};