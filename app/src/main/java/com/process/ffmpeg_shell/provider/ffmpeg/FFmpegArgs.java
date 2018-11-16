package com.process.ffmpeg_shell.provider.ffmpeg;

/**
 * ffmpeg参数列表
 * Created by kerwin on 2018/11/15
 */
public class FFmpegArgs {
    // --------------------通用命令选项--------------------
    /** 通用命令选项: license */
    public static final String COMMON_L = "-L";
    /** 通用命令选项: 帮助 */
    public static final String COMMAND_H = "-h";
    /** 通用命令选项: 显示可用的文件封装格式, 编解码, 协议等. 其中D为Decode或Demux, E为Encode或者Mux. */
    public static final String COMMAND_FORMATS = "-formats";
    /** 通用命令选项: 相对于-formats只显示编解码格式 */
    public static final String COMMAND_CODECS = "-codecs";
    /** 通用命令选项: 强迫采用格式fmt(文件格式, 如wav, avi等). 如果不指定该选项, 则使用文件扩展名来自动探测格式. */
    public static final String COMMAND_F = "-f";
    /** 通用命令选项: 输入文件*/
    public static final String COMMAND_I = "-i";
    /** 通用命令选项: 覆盖输出文件 */
    public static final String COMMAND_Y = "-y";
    /** 通用命令选项: duration设置录制/转码的时长. hh:mm:ss[.xxx]格式的记录时间也支持 */
    public static final String COMMAND_T = "-t";
    /** 通用命令选项: position搜索到指定的起始时间 [-]hh:mm:ss[.xxx]的格式也支持 */
    public static final String COMMAND_SS = "-ss";
    /** 通用命令选项: string设置标题(本人编译的linux版本未带此选项) */
    public static final String COMMAND_TITLE = "-title";
    /** 通用命令选项: string设置本人(本人编译的linux版本未带此选项) */
    public static final String COMMAND_AUTHOR = "-author";
    /** 通用命令选项: string设置版权(本人编译的linux版本未带此选项) */
    public static final String COMMAND_COPYRIGHT = "-copyright";
    /** 通用命令选项: string 设置评论(本人编译的linux版本未带此选项) */
    public static final String COMMAND_COMMENT = "-comment";
    /**
     * 通用命令选项: type 设置目标文件类型(type可以为: vcd, svcd, dvd, "dv", "dv50")所有的格式选项(比特率, 编解码以及缓冲区大小)
     * 自动设置, 只需要输入如下的就可以了: ffmpeg -i myfile.avi -target vcd /tmp/vcd.mpg. 另外, type也可以加前缀: "pal-",
     * "ntsc-" or "film-". */
    public static final String COMMAND_TARGET = "-target";
    /** 通用命令选项:激活高质量设置(本人编译的linux版本未带此选项)*/
    public static final String COMMAND_HQ = "-hq";
    /**
     * 通用命令选项: offset 设置以秒为基准的时间偏移, 该选项影响所有后面的输入文件. 该偏移被加到输入文件的时戳,
     * 定义一个正偏移意味着相应的流被延迟了 offset秒. [-]hh:mm:ss[.xxx]的格式也支持 */
    public static final String COMMAND_ITSOFFSET = "-itsoffset";
    // --------------------------------------------------

    // --------------------视频命令选项--------------------
    /** 视频命令选项: bitrate 设置比特率, 缺省200kb/s(该数值依据转发的文章, 本人未验证).*/
    public static final String VIDEO_BITRATE = "-b:v";
    /** 视频命令选项: fps设置帧率. 缺省25(该数值依据转发的文章, 本人未验证) */
    public static final String VIDEO_R = "-r";
    /** 视频命令选项: size设置帧大小格式为WXH缺省160X128.默认为与源相同大小.下面的简写也可以直接使用: Sqcif 128X96 qcif 176X144 cif 252X288 4cif 704X576 */
    public static final String VIDEO_S = "-s";
    /** 视频命令选项: aspect设置横纵比 4:3 16:9 或 1.3333 1.7777 */
    public static final String VIDEO_ASPECT = "-aspect";
    /** 视频命令选项: size设置顶部切除带大小像素单位 */
    public static final String VIDEO_CROPTOP = "-croptop";
    /** 视频命令选项: size -cropleft size -cropright size */
    public static final String VIDEO_CROPBOTTOM = "-cropbottom";
    /** 视频命令选项: size设置顶部补齐的大小 像素单位 */
    public static final String VIDEO_PADTOP = "-padtop";
    /** 视频命令选项: size */
    public static final String VIDEO_PADBOTTOM = "-padbottom";
    /** 视频命令选项: size */
    public static final String VIDEO_PADLEFT = "-padleft";
    /** 视频命令选项: color设置补齐条颜色(hex,6个16进制的数, 红:绿:兰排列, 比如 000000代表黑色)*/
    public static final String VIDEO_PADCOLOR = "-padcolor";
    /** 视频命令选项: 不做视频记录 */
    public static final String VIDEO_VN = "-vn";
    /** 视频命令选项: tolerance设置视频码率容忍度kbit/s */
    public static final String VIDEO_BT = "-bt";
    /** 视频命令选项: bitrate设置最大视频码率容忍度 */
    public static final String VIDEO_MAXRATE = "-maxrate";
    /** 视频命令选项: bitreate设置最小视频码率容忍度 */
    public static final String VIDEO_MINRATE = "-minrate";
    /** 视频命令选项: size设置码率控制缓冲区大小 */
    public static final String VIDEO_BUFSIZE = "-bufsize";
    /** 视频命令选项: codec强制使用codec编解码方式.如果用copy表示原始编解码数据必须被拷贝*/
    public static final String VIDEO_VCODEC = "-vcodec";
    /** 视频命令选项: 使用同样视频质量作为源(VBR). */
    public static final String VIDEO_SAMEQ = "-sameq";
    /** 视频命令选项: n选择处理遍数(1或者2).两遍编码非常有用.第一遍生成统计信息,第二遍生成精确的请求的码率*/
    public static final String VIDEO_PASS = "-pass";
    /** 视频命令选项: file选择两遍的纪录文件名为file */
    public static final String VIDEO_PASSLOGFILE = "-passlogfile";
    // --------------------------------------------------

    // --------------------高级视频命令选项--------------------
    /** 高级视频命令选项：gop_size设置图像组大小 */
    public static final String VIDEO_HIGH_G = "-g";
    /** 高级视频命令选项：仅适用帧内编码 */
    public static final String VIDEO_HIGH_INTRA = "-intra";
    /** 高级视频命令选项：q使用固定的视频量化标度(VBR)*/
    public static final String VIDEO_HIGH_QSCALE = "-qscale";
    /** 高级视频命令选项: q最小视频量化标度(VBR)*/
    public static final String VIDEO_HIGH_QMIN = "-qmin";
    /** 高级视频命令选项: q最大视频量化标度(VBR)*/
    public static final String VIDEO_HIGH_QMAX = "-qmax";
    /** 高级视频命令选项: q量化标度间最大偏差 (VBR)*/
    public static final String VIDEO_HIGH_QDIFF = "-qdiff";
    /** 高级视频命令选项: blur视频量化标度柔化(VBR)*/
    public static final String VIDEO_HIGH_QBLUR = "-qblur";
    /** 高级视频命令选项: compression视频量化标度压缩(VBR) */
    public static final String VIDEO_HIGH_QCOMP = "-qcomp";
    /** 高级视频命令选项: complexity一遍编码的初始复杂度 */
    public static final String VIDEO_HIGH_RC_INIT_CPLX = "-rc_init_cplx";
    /** 高级视频命令选项: factor在p和b帧间的qp因子 */
    public static final String VIDEO_HIGH_B_QFACTOR = "-b_qfactor";
    /** 高级视频命令选项: factor在p和i帧间的qp因子 */
    public static final String VIDEO_HIGH_I_QFACTOR = "-i_qfactor";
    /** 高级视频命令选项: offset在p和b帧间的qp偏差 */
    public static final String VIDEO_HIGH_B_QOFFSET = "-b_qoffset";
    /** 高级视频命令选项: offset在p和i帧间的qp偏差 */
    public static final String VIDEO_HIGH_I_QOFFSET = "-i_qoffset";
    /** 高级视频命令选项: equation设置码率控制方程 默认tex^qComp */
    public static final String VIDEO_HIGH_RC_EQ = "-rc_eq";
    /** 高级视频命令选项：override特定间隔下的速率控制重载 */
    public static final String VIDEO_HIGH_RC_OVERRIDE = "-rc_override";
    /** 高级视频命令选项: method设置运动估计的方法可用方法有zero phods log x1 epzs(缺省) full*/
    public static final String VIDEO_HIGH_ME = "-me";
    /**
     * 高级视频命令选项：algo设置dct的算法可用的有 0 FF_DCT_AUTO 缺省的DCT 1 FF_DCT_FASTINT 2 FF_DCT_INT 3
     * FF_DCT_MMX 4 FF_DCT_MLIB 5 FF_DCT_ALTIVEC
     * */
    public static final String VIDEO_HIGH_DCT_ALGO = "-dct_algo";
    /**
     * 高级视频命令选项：algo 设置idct算法. 可用的有 0 FF_IDCT_AUTO 缺省的IDCT 1 FF_IDCT_INT 2 FF_IDCT_SIMPLE
     * 3 FF_IDCT_SIMPLEMMX 4 FF_IDCT_LIBMPEG2MMX 5 FF_IDCT_PS2 6 FF_IDCT_MLIB 7 FF_IDCT_ARM 8
     * FF_IDCT_ALTIVEC 9 FF_IDCT_SH4 10 FF_IDCT_SIMPLEARM
     * */
    public static final String VIDEO_HIGH_IDCT_ALGO = "-idct_algo";
    /** 高级视频命令选项：n设置错误残留为n 1 FF_ER_CAREFULL 缺省 2 FF_ER_COMPLIANT 3 FF_ER_AGGRESSIVE 4 FF_ER_VERY_AGGRESSIVE */
    public static final String VIDEO_HIGH_ER = "-er";
    /** 高级视频命令选项：bit_mask 设置错误掩蔽为bit_mask,该值为如下值的位掩码 1 FF_EC_GUESS_MVS (default=enabled) 2 FF_EC_DEBLOCK (default=enabled)*/
    public static final String VIDEO_HIGH_EC = "-ec";
    /** 高级视频命令选项：frames 使用frames B 帧, 支持mpeg1,mpeg2,mpeg4*/
    public static final String VIDEO_HIGH_BF = "-bf";
    /** 高级视频命令选项: mode宏块决策 0 FF_MB_DECISION_SIMPLE 使用mb_cmp 1 FF_MB_DECISION_BITS 2 FF_MB_DECISION_RD*/
    public static final String VIDEO_HIGH_MBD = "-mbd";
    /** 高级视频命令选项: 使用4个运动矢量仅用于mpeg4 */
    public static final String VIDEO_HIGH_4MV = "-4mv";
    /** 高级视频命令选项：使用数据划分仅用于mpeg4 */
    public static final String VIDEO_HIGH_PART = "-part";
    /** 高级视频命令选项：param绕过没有被自动监测到编码器的问题 */
    public static final String VIDEO_HIGH_BUG = "-bug";
    /** 高级视频命令选项：strictness跟标准的严格性*/
    public static final String VIDEO_HIGH_STRICT = "-strict";
    /** 高级视频命令选项: 使能高级帧内编码h263+*/
    public static final String VIDEO_HIGH_AIC = "-aic";
    /** 高级视频命令选项: 使能无限运动矢量 h263+*/
    public static final String VIDEO_HIGH_UMV = "-umv";
    /** 高级视频命令选项: 不采用交织方法*/
    public static final String VIDEO_HIGH_DEINTERLACE = "-deinterlace";
    /** 高级视频命令选项: 强迫交织法编码仅对mpeg2和mpeg4有效.当你的输入是交织的并且你想要保持交织以最小图像损失的时候采用该选项.可选的方法是不交织,但是损失更大*/
    public static final String VIDEO_HIGH_INTERLACE = "-interlace";
    /** 高级视频命令选项: 计算压缩帧的psnr */
    public static final String VIDEO_HIGH_PSNR = "-psnr";
    /** 高级视频命令选项: 输出视频编码统计到vstats_hhmmss.log */
    public static final String VIDEO_HIGH_VSTATS = "-vstats";
    /** 高级视频命令选项: module插入视频处理模块module包括了模块名和参数,用空格分开 */
    public static final String VIDEO_HIGH_vhook = "vhook";
    // --------------------------------------------------

    // --------------------音频命令选项--------------------
    /** 音频命令选项：bitrate设置音频码率.有些版本是(-b:a bitrate)*/
    public static final String VOICE_AB = "-ab";
    /** 音频命令选项: freq设置音频采样率*/
    public static final String VOICE_AR = "-ar";
    /** 音频命令选项: channels设置通道缺省为与输入相同 */
    public static final String VOICE_AC = "-ac";
    /** 音频命令选项: 不使能音频纪录 */
    public static final String VOICE_AN = "-an";
    /** 音频命令选项: codec使用codec编解码 */
    public static final String VOICE_ACODEC = "-acodec";
    // --------------------------------------------------

    // --------------------音频/视频捕获命令选项--------------------
    /** 音频/视频捕获命令: device设置视频捕获设备. 比如/dev/video0*/
    public static final String CAPTURE_VD = "-vd";
    /** 音频/视频捕获命令: channel设置视频捕获通道 DV1394专用*/
    public static final String CAPTURE_VC = "-vc";
    /** 音频/视频捕获命令: standard设置电视标准 NTSC PAL(SECAM)*/
    public static final String CAPTURE_TVSTD = "-tvstd";
    /** 音频/视频捕获命令: 设置DV1394捕获*/
    public static final String CAPTURE_DV1394 = "-dv1394";
    /** 音频/视频捕获命令: device设置音频设备比如/dev/dsp*/
    public static final String CAPTURE_AV = "-av";
    // --------------------------------------------------

    // --------------------高级命令选项--------------------
    /** 高级命令选项: file:stream 设置输入流映射*/
    public static final String HIGH_MAP = "-map";
    /** 高级命令选项: 打印特定调试信息*/
    public static final String HIGH_DEBUG = "-debug";
    /** 高级命令选项: 为基准测试加入时间*/
    public static final String HIGH_BENCHMARK = "-benchmark";
    /** 高级命令选项: 倾倒每一个输入包*/
    public static final String HIGH_HEX = "-hex";
    /** 高级命令选项: 仅使用位精确算法用于编解码测试*/
    public static final String HIGH_BITEXACT = "-bitexact";
    /** 高级命令选项: size设置包大小, 以bits为单位*/
    public static final String HIGH_PS = "-ps";
    /** 高级命令选项: 以本地帧频读数据, 主要用于模拟捕获设备*/
    public static final String HIGH_RE = "-re";
    /** 高级命令选项: 循环输入流(只工作于图像流, 用于ffserver测试)*/
    public static final String HIGH_LOOP = "-loop";
    // --------------------------------------------------
}
