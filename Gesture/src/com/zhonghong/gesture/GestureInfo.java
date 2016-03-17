package com.zhonghong.gesture;

public class GestureInfo {
  public static final String G_CMD_CONTINUE = "continue";
  public static final String G_CMD_OPEN_APP = "open_app";
  public static final String G_CMD_SYSTEMKEY = "systemkey";
  public static final String G_DATA_KEY_BACK = "key_back";
  public static final String G_DATA_KEY_HOME = "key_home";
  public static final String G_DATA_KEY_MENU = "key_menu";
  public static final String G_DATA_KEY_MUTE = "key_mute";
  public static final String G_DATA_KEY_NAVI = "key_navi";
  public static final String G_DATA_KEY_NEXT = "key_next";
  public static final String G_DATA_KEY_PRE = "key_pre";
  public static final String G_DATA_KEY_SPEECH = "key_speech";
  public static final String G_DATA_VOLUME = "volume";
  public static final String G_NAME_CONTINUE_HORIZONTAL = "_continue_horizontal";
  public static final String G_NAME_CONTINUE_VERTICAL = "_continue_vertical";
  public static final String G_NAME_INC = "_increase";
  public static final String G_NAME_DEC = "_decrease";
  public static final String G_NAME_DOWN = "_down";
  public static final String G_NAME_DRAW = "_draw";
  public static final String G_NAME_FOUR_CONTINUE_HORIZONTAL = "4_continue_horizontal";
  public static final String G_NAME_FOUR_CONTINUE_VERTICAL = "4_continue_vertical";
  public static final String G_NAME_FOUR_DOWN = "4_down";
  public static final String G_NAME_FOUR_LEFT = "4_left";
  public static final String G_NAME_FOUR_RIGHT = "4_right";
  public static final String G_NAME_FOUR_UP = "4_up";
  public static final String G_NAME_LEFT = "_left";
  public static final String G_NAME_ONE_DRAW = "1_draw";
  public static final String G_NAME_RIGHT = "_right";
  public static final String G_NAME_THREE_CONTINUE_HORIZONTAL = "3_continue_horizontal";
  public static final String G_NAME_THREE_CONTINUE_VERTICAL = "3_continue_vertical";
  public static final String G_NAME_THREE_DOWN = "3_down";
  public static final String G_NAME_THREE_LEFT = "3_left";
  public static final String G_NAME_THREE_RIGHT = "3_right";
  public static final String G_NAME_THREE_UP = "3_up";
  public static final String G_NAME_TWO_CONTINUE_HORIZONTAL = "2_continue_horizontal";
  public static final String G_NAME_TWO_CONTINUE_VERTICAL = "2_continue_vertical";
  public static final String G_NAME_TWO_DOWN = "2_down";
  public static final String G_NAME_TWO_LEFT = "2_left";
  public static final String G_NAME_TWO_RIGHT = "2_right";
  public static final String G_NAME_TWO_UP = "2_up";
  public static final String G_NAME_UP = "_up";
  public static final String G_NAME_5_POINT_IN = "5_point_in";
  public String cmd;
  public String data;
  public String name;

  public GestureInfo(String paramString1, String paramString2, String paramString3)
  {
    this.name = paramString1;
    this.cmd = paramString2;
    this.data = paramString3;
  }
	
}
