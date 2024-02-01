<template>
  <div class="MainBox">
    <!--菜单栏组件-->
    <myMenuBar ref="menuData" @transfer="getLoginInfo"></myMenuBar>
    <!--聊天窗口组件-->
    <el-container style="height: 100%; border: 1px solid #eee">
      <!--边栏-->
      <el-aside width="300px" style="background-color: rgb(238, 241, 246)">
        <!--搜索框-->
        <div class="search-box">
          <el-form>
            <el-form-item>
              <el-input
                  class="search-input"
                  v-model="searchData.username"
                  placeholder="搜索或添加好友"
                  @input="preGetTableData"
                  style="width: 200px"
              ></el-input>
              <!--添加好友（创造team）-->
              <el-popover
                  placement="bottom"
                  title="添加好友"
                  width="200"
                  trigger="manual"
                  v-model="notices.isAddTeamVisible"
              >
                <!--检索结果弹框-->
                <div>
                  <!--头像组件-->
                  <el-avatar
                      :size="80"
                      fit="cover"
                      style="vertical-align: top"
                      :src="searchData.avatarUrl"
                      @error="true"
                  >
                    <img :src="emptyAvatar"/>
                  </el-avatar>
                  <!--个人信息-->
                  <span> {{ searchData.username }} </span>
                  <span>{{ searchData.gender }}</span>
                  <p>{{ searchData.profile }}</p>
                  <!--按钮-->
                  <div style="text-align: right; margin: 0">
                    <el-button
                        size="mini"
                        type="text"
                        @click="notices.isAddTeamVisible = false"
                    >取消
                    </el-button
                    >
                    <el-button type="primary" size="mini" @click="addUser"
                    >确定
                    </el-button
                    >
                  </div>
                </div>
                <el-button
                    circle
                    icon="el-icon-plus"
                    slot="reference"
                    @click="preAddUser"
                    style="margin-left: 10px"
                ></el-button>
              </el-popover>
            </el-form-item>
          </el-form>
        </div>
        <!--可聊天的对象-->
        <el-table
            :data="pageInfo.tableData"
            highlight-current-row
            @current-change="handleTableChange"
            :show-header="false"
        >
          <el-table-column prop="teamImage" label="用户头像" width="80px">
            <template slot-scope="scope">
              <el-avatar
                  :size="70"
                  fit="cover"
                  :src="scope.row.teamImage"
              ></el-avatar>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="团队名"></el-table-column>
          <el-table-column
              class="last-chat-time"
              prop="updateTime"
              label="最后聊天时间"
          >
          </el-table-column>
        </el-table>
        <!--分页条-->
        <el-pagination
            background
            layout="prev, next"
            :total="pageInfo.totalSize"
            :page-size="6"
            :current-page="pageInfo.pageNow"
            @current-change="handlePaginationChange"
            :hide-on-single-page="false"
        >
        </el-pagination>
      </el-aside>

      <el-main
          v-if="currentRow == null"
          style="display: flex; align-items: center; justify-content: center"
      >
        <el-empty
            :image="emptyRoom"
            description=" "
            :image-size="200"
            style="
            filter: grayscale(1) contrast(50%) saturate(1%);
            margin: auto;
          "
        ></el-empty>
      </el-main>

      <el-main v-else style="position: relative; overflow: hidden">
        <!-- 聊天对象信息 -->
        <div style="width: auto; height: 100px">
          <div style="float: left">
            <!-- 头像组件 -->
            <el-avatar
                shape="square"
                :size="80"
                fit="cover"
                style="vertical-align: middle"
                :src="currentRow.teamImage"
            ></el-avatar>
            <!-- 名字 -->
            <span> {{ currentRow.name }}</span>
          </div>
          <div
              style="
              width: auto;
              height: 80px;
              float: right;
              vertical-align: middle;
            "
          >
            <!--建群/添加好友入群按钮-->
            <el-button
                circle
                type="primary"
                icon="el-icon-plus"
                @click="preAddUserForGroup()"
                style="text-align: center; margin: 19px 8px"
            ></el-button>
            <!--建群筛选页面-->
            <el-dialog
                width="400px"
                :title="currentRow.joinNum == 2 ? '组建群聊' : '添加好友入群'"
                :visible.sync="notices.isAddMemberVisible"
            >
              <el-table
                  ref="multipleTable"
                  height="400px"
                  v-el-table-infinite-scroll="load"
                  :data="privateTeamPageInfo.tableData"
                  tooltip-effect="dark"
                  @selection-change="handleSelectionChange"
              >
                <el-table-column type="selection" :selectable="rowSelectable">
                </el-table-column>
                <el-table-column prop="teamImage">
                  <template slot-scope="scope">
                    <el-avatar
                        :size="70"
                        fit="cover"
                        :src="scope.row.img"
                    ></el-avatar>
                  </template>
                </el-table-column>
                <el-table-column prop="name"></el-table-column>
              </el-table>
              <div slot="footer">
                <el-button @click="notices.isAddMemberVisible = false"
                >取 消
                </el-button
                >
                <el-button type="primary" @click="addUserForGroup()"
                >确 定
                </el-button
                >
              </div>
            </el-dialog>
            <!--清空页面按钮-->
            <el-button
                circle
                type="danger"
                icon="el-icon-delete"
                @click="clearPage"
                style="text-align: center; margin: 19px 8px"
            >
            </el-button>
            <!--更多按钮-->
            <el-button
                circle
                icon="el-icon-more-outline"
                @click="notices.isMoreDrawerVisible = true"
                style="text-align: center; margin: 19px 8px"
            >
            </el-button>
            <!--更多信息界面-->
            <el-drawer
                title="群管理"
                :visible.sync="notices.isMoreDrawerVisible"
                style="position: absolute"
                :modal="false"
            >
              <span>群成员展示+群成员删除+群主+群主转让+群解散</span>
            </el-drawer>
          </div>
        </div>

        <!--对话框 聊天框-->
        <div class="chat-content" ref="chatContent">
          <!-- nowPageMessage 当前聊天页面数组-->
          <div v-for="(item, index) in nowPageMessage" :key="index">
            <!-- 对方发送的信息：send不是我，receive是我 -->
            <div
                class="word"
                v-if="
                //满足基本显示条件
                item.sendUserId != loginData.id &&
                item.receiveUserId == loginData.id &&
                item.isSpecial == undefined
                //满足忽略条件
                // && item.isSpecial != undefined &&
                // ignoreMessageIds[item.teamId].includes(item.id) == false
              "
            >
              <img :src="currentRow.teamImage"/>
              <div class="info">
                <p class="time">{{ currentRow.name }} {{ item.sendTime }}</p>
                <div class="info-content">{{ item.content }}</div>
              </div>
            </div>
            <!-- 我发送的信息：send是我，receive不是我 -->
            <div
                class="word-my"
                v-else-if="
                //满足显示条件
                item.sendUserId == loginData.id &&
                item.receiveUserId != loginData.id &&
                item.isSpecial == undefined
                //满足忽略条件
                // && item.isSpecial != undefined &&
                // ignoreMessageIds[item.teamId].includes(item.id) == false
              "
            >
              <div class="info">
                <p class="time">{{ loginData.username }} {{ item.sendTime }}</p>
                <div
                    class="info-content"
                    :style="msgIsRead(item)"
                    @contextmenu.prevent="onContextmenu(item)"
                >
                  {{ item.content }}
                </div>
              </div>
              <img :src="loginData.url"/>
            </div>
            <!-- 历史记录分隔线 -->
            <div v-else-if="item.isSpecial" style="text-align: center">
              <el-divider content-position="center">{{ item.info }}</el-divider>
            </div>
          </div>
        </div>
        <!--若无历史聊天记录-->
        <!-- <div
          style="
            width: 100%;
            height: 400px;
            padding: 20px;
            background-color: rgb(238, 241, 246);
          "
        >
          <el-empty
            description="暂无历史消息"
            :image-size="200"
            style="margin: auto; padding: auto"
          ></el-empty>
        </div> -->
        <!-- <transition name="el-zoom-in-bottom">
          <el-button
            icon="el-icon-bottom"
            plain
            v-show="unReadMessageNum[currentRow.teamId] > 0"
            >{{ unReadMessageNum[currentRow.teamId] }}未读</el-button
          >
        </transition> -->
        <el-divider></el-divider>

        <!--发送消息-->
        <div>
          <el-popover
              placement="bottom"
              width="200"
              trigger="manual"
              content="发送消息不可为空"
              v-model="notices.isNullMessageSend"
          >
            <el-input
                slot="reference"
                type="textarea"
                autosize
                placeholder="输入要发送给对方的文本...（按下enter键发送，alt+enter换行）"
                v-model="textArea"
                @keyup.enter.native="KeyPressed"
                style="width: 100%; height: 50%"
            >
            </el-input>
          </el-popover>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import axios from "axios";
import qs from "qs";
import Cookies from "js-cookie";
import elTableInfiniteScroll from "el-table-infinite-scroll";
import {debounce} from "lodash";
import myMenuBar from "../components/myMenuBar.vue";

export default {
  directives: {
    "el-table-infinite-scroll": elTableInfiniteScroll,
  },
  components: {
    myMenuBar,
  },
  data() {
    return {
      ws: null,
      emptyAvatar: require("../assets/emptyAvatar.png"),
      emptyRoom: require("../assets/hidrinkLOGO.svg"),
      notices: {
        isAddTeamVisible: false,
        isMoreDrawerVisible: false,
        isAddMemberVisible: false,
        isNullMessageSend: false,
        isErrorClose: true,
      },
      loginData: {
        isLogin: false,
        id: 0,
        url: "头像",
        role: "user",
        username: "null",
      },
      pageInfo: {
        /*当前页面总数据*/
        username: "",
        totalSize: 0,
        pageSize: 6,
        pageNow: 1,
        tableData: [] /*表格数据，需要一定是一个数组*/,
      },
      searchData: {
        userId: 0,
        name: "",
        avatarUrl: "头像链接",
        gender: "性别",
        profile: "个人介绍",
      },
      //被选中的团队信息json
      currentRow: null,
      subTeamIds: [], //群主与团队成员之间的teamId
      //要发送给他人的信息
      textArea: "",
      //所有聊天记录  当前聊天框的聊天记录
      messages: [],
      nowPageMessage: [],
      //聊天框中不显示的messageIds，下标代表teamId；稀疏数组，方案不佳待优化
      // ignoreMessageIds: [[]],
      //添加好友入群数据：1.所有团队数据 2.被选中的团队数据
      privateTeamPageInfo: {
        /*当前页面总数据*/
        totalSize: 0,
        pageSize: 6,
        pageNow: 1,
        tableData: [] /*表格数据，需要一定是一个数组*/,
        selectableIds: [] /*建群时，可以选择的邀请的人的teamId数组*/,
      },
      selectedTeamRow: [],
    };
  },
  methods: {
    /*获取登录信息后，填充表格信息*/
    getLoginInfo(loginData) {
      this.loginData.id = loginData.id;
      this.loginData.isLogin = loginData.isLogin;
      this.loginData.username = loginData.username;
      console.log("RoomVue Get Login Data: ");
      console.log(this.loginData);
      this.CreateWebSocket();
    },
    scrollToBottom() {
      var that = this;
      that.$nextTick(() => {
        that.$refs.chatContent.scrollTop = that.$refs.chatContent.scrollHeight;
      });
    },
    timestampToDateTime(timestamp) {
      // 将时间戳转换为毫秒
      const date = new Date(timestamp);
      // 格式化日期和时间
      const dateTimeString = date.toLocaleString();
      return dateTimeString;
    },
    CreateWebSocket() {
      var that = this;
      this.ws = new WebSocket("ws://localhost:8090/chat");
      this.ws.onopen = function () {
        console.log("Open WebSocket Success!");
        //获取表格数据
        that.getTableData();
      };
      this.ws.onmessage = function (evt) {
        var serverData = JSON.parse(evt.data);
        //后端传来数据
        // console.log("Get Message: ");
        // console.log(serverData);
        var serverResponse;
        if (serverData.code === 0) {
          //后端传来异常信息
          //情况1：对面不在线
          // that.$message({
          //   message: serverData.message,
          //   type: "error",
          // });
        } else if (serverData.code === 4) {
          //后端传来聊天信息
          serverResponse = serverData.data; //后端data区数据
          if (that.messages[serverResponse.teamId] != null) {
            //处理时间时间戳
            serverResponse.sendTime = that.timestampToDateTime(
                serverResponse.sendTime
            );
            that.messages[serverResponse.teamId][0].push(serverResponse);
            that.nowPageMessage = that.messages[serverResponse.teamId][0];
            console.log("Get New Page Messages: ");
            console.log(that.nowPageMessage);
            //跳转到页面最下端
            that.scrollToBottom();
          } else {
            that.messages[serverResponse.teamId] = [];
            that.messages[serverResponse.teamId].push(serverResponse);
          }
        } else if (serverData.code === 3) {
          //后端传来撤回信息的id
          //则应当删除messages[serverResponse.teamId][0]和that.nowPageMessage中相应id的message
          serverResponse = JSON.parse(serverData.data); //后端data区数据
          console.log("Get Withdraw Message: ");
          console.log(serverResponse);
          var delMsgId = serverResponse.messageId;
          var delUsername = serverResponse.username;
          //二分查找删除
          let low = 0,
              high = that.nowPageMessage.length - 1;
          while (low <= high) {
            let mid = Math.floor((low + high) / 2);
            if (that.nowPageMessage[mid].messageId < delMsgId) {
              low = mid + 1;
            } else if (that.nowPageMessage[mid].messageId > delMsgId) {
              high = mid - 1;
            } else {
              console.log("Delete Message: ");
              console.log(that.nowPageMessage[mid]);
              that.nowPageMessage[mid].content = delUsername + "撤回了一条信息";
              that.nowPageMessage[mid].isSpecial = true;
              that.nowPageMessage[mid].info = delUsername + "撤回了一条信息";
              break;
            }
          }
          //二分查找结束后，添加撤回提示语
          // that.nowPageMessage.push({
          //   isSpecial: true,
          //   info: delUsername + "撤回了一条信息",
          // });
        }
      };
      this.ws.onclose = function () {
        if (!that.isErrorClose) {
          return;
        }
        that
            .$confirm("账号状态异常，请重新登录", "提示", {
              confirmButtonText: "好的",
              type: "info",
              showClose: false,
              closeOnClickModal: false,
              closeOnPressEscape: false,
              closeOnHashChange: false,
            })
            .then(() => {
              that.$router.push("/login");
            })
            .catch(() => {
              that.$router.push("/login");
            });
        console.log("WebSocket Closed!");
      };
      this.ws.onerror = function () {
        that.$message.error("出错了，请联系后台管理人员");
      };
    },
    preGetTableData: debounce(function () {
      // this.pageInfo.username = this.searchData.username;
      // this.getTableData();
      this.$message('有关下方表格的筛选搜索待重做');
    }, 500),
    getTableData() {
      var that = this;
      //没必要发送现有的团队信息，清空数据
      this.pageInfo.tableData = [];
      let getAllTeamUrl =
          that.GLOBAL.networkSrc + "/getAllTeam?" + qs.stringify(that.pageInfo);
      var config = {
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
          token: Cookies.get("token"),
        },
      };
      var serverResponse;
      axios
          .get(getAllTeamUrl, config)
          .then(function (result) {
            serverResponse = JSON.parse(result.request.response);
            console.log("Get Team List:");
            console.log(serverResponse.data);
            if (serverResponse.code == 1) {
              if (serverResponse.data.tableData != null) {
                that.pageInfo = serverResponse.data;
                //整理日期格式
                that.pageInfo.tableData.forEach((item) => {
                  if (item.updateTime != null) {
                    item.updateTime = item.updateTime.replace("T00:00:00", "");
                  }
                });
              } else {
                //若无数据，初始化表格
                that.pageInfo.tableData = [];
                that.pageInfo.pageNow = 1;
                that.pageInfo.totalSize = 0;
                that.pageInfo.pageSize = 6;
              }
            } else {
              that.$message({
                type: "error",
                message: serverResponse.message,
                duration: 1500,
              });
              console.log(serverResponse.message);
            }
          })
          .catch((error) => {
            console.log("出错了，请联系后台管理人员" + error);
          });
    },
    preAddUser() {
      //查找新好友
      var that = this;
      //1.过滤无效信息
      if (this.searchData.username == "" || this.searchData.username == null) {
        that.$message({
          type: "error",
          message: "添加好友的用户名不能为空",
          duration: 1500,
        });
      } else {
        //2.发送请求
        let searchNewUserUrl =
            that.GLOBAL.networkSrc +
            "/searchUser?username=" +
            that.searchData.username;
        var config = {
          headers: {
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "*",
            token: Cookies.get("token"),
          },
        };
        var serverResponse;
        axios
            .get(searchNewUserUrl, config)
            .then(function (result) {
              serverResponse = JSON.parse(result.request.response);
              console.log("Get User Data:");
              console.log(serverResponse.data);
              //3.展示查找结果信息
              if (serverResponse.data != null) {
                that.searchData = serverResponse.data;
                that.notices.isAddTeamVisible = true;
              } else {
                that.$message({
                  type: "error",
                  message: "未找到用户信息",
                  duration: 1500,
                });
                console.log(serverResponse.message);
              }
            })
            .catch((error) => {
              console.log("出错了，请联系后台管理人员" + error);
            });
      }
    },
    addUser() {
      //决定添加好友到好友列表中
      var that = this;
      let newTeamUrl =
          that.GLOBAL.networkSrc + "/newTeam/" + that.searchData.userId;
      //1.发送请求
      var config = {
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
          token: Cookies.get("token"),
        },
      };
      var serverResponse;
      axios
          .get(newTeamUrl, config)
          .then(function (result) {
            serverResponse = JSON.parse(result.request.response);
            console.log("Get Response:");
            console.log(serverResponse.data);
            //2.通知结果
            if (serverResponse.code == 1) {
              that.$message({
                type: "success",
                message: "添加好友成功",
                duration: 1500,
              });
              that.notices.isAddTeamVisible = false;
              that.getTableData();
              that.currentRow = null;
            } else {
              that.$message({
                type: "error",
                message: serverResponse.message,
                duration: 1500,
              });
              console.log(serverResponse.message);
            }
          })
          .catch((error) => {
            console.log("出错了，请联系后台管理人员" + error);
          });
    },
    handleTableChange(val) {
      //val 是当前行数据的所有信息，点击某一对话框瞬间会获取历史信息
      var that = this;
      //1.发送请求
      let getHistoryMessagesUrl =
          that.GLOBAL.networkSrc + "/getHistoryMessages/" + val.teamId;
      var config = {
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
          token: Cookies.get("token"),
        },
      };
      var serverResponse;
      axios
          .get(getHistoryMessagesUrl, config)
          .then(function (result) {
            serverResponse = JSON.parse(result.request.response);
            //2.通知结果
            if (serverResponse.code == 1) {
              if (
                  serverResponse.data != null &&
                  that.messages[val.teamId] != null
              ) {
                that.messages[val.teamId].push(serverResponse.data);
              } else if (
                  serverResponse.data != null &&
                  that.messages[val.teamId] == null
              ) {
                that.messages[val.teamId] = [];
                that.messages[val.teamId].push(serverResponse.data);
              } else if (
                  serverResponse.data == null &&
                  that.messages[val.teamId] == null
              ) {
                that.messages[val.teamId] = [];
              }
              that.nowPageMessage = that.messages[that.currentRow.teamId][0];
              console.log("Now Page Messages: ");
              console.log(that.nowPageMessage);
              //整理日期格式【写了一个bug但可以正常运行？！】
              /*由于that.nowPageMessage数组内含分隔符要显示的文本，则没有时间戳字段sendTime，
              就会因为undefined而报错进入catch，刚好跳过第二次的多余的“以上是历史记录”的提示，刚好解决问题*/
              for (var i = 0; i < that.nowPageMessage.length; i++) {
                if (that.nowPageMessage[i].sendTime.includes("T")) {
                  that.nowPageMessage[i].sendTime = that.nowPageMessage[
                      i
                      ].sendTime.replace("T00:00:00", "");
                }
              }
              //添加历史记录分隔线标记
              if (that.nowPageMessage.length > 0) {
                that.nowPageMessage.push({
                  isSpecial: true,
                  info: "以上是历史记录",
                });
              }
              //加载完消息记录后，跳转到页面最下端
              that.scrollToBottom();
            } else {
              that.$message({
                type: "error",
                message: serverResponse.message,
                duration: 1500,
              });
              console.log(serverResponse.message);
            }
          })
          .catch((error) => {
            if (
                error ==
                "TypeError: Cannot read properties of undefined (reading 'includes')"
            ) {
              that.scrollToBottom();
            } else {
              console.log("出错了，请联系后台管理人员" + error);
            }
          });
      this.currentRow = val;
      this.subTeamIds = [];
      if (this.currentRow.joinNum == 2) {
        that.subTeamIds[0] = that.currentRow.teamId;
      } else {
        //获取成员的team的Id
        this.getSubTeamId();
      }
    },
    getSubTeamId() {
      //主要是获得子Team的id
      var that = this;
      let getSubTeamIdUrl =
          that.GLOBAL.networkSrc +
          "/getSubTeamId/" +
          that.loginData.id +
          "/" +
          that.currentRow.teamId;
      var config = {
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
          token: Cookies.get("token"),
        },
      };
      var serverResponse;
      axios
          .get(getSubTeamIdUrl, config)
          .then(function (result) {
            serverResponse = JSON.parse(result.request.response);
            console.log("Get Sub Team Id(s):");
            console.log(serverResponse.data);
            if (serverResponse.code == 1) {
              //载入subTeamIds数组
              that.subTeamIds = serverResponse.data;
            } else {
              that.$message({
                type: "error",
                message: serverResponse.message,
                duration: 1500,
              });
              console.log(serverResponse.message);
            }
          })
          .catch((error) => {
            console.log("出错了，请联系后台管理人员" + error);
          });
    },
    msgIsRead(msgItemData) {
      if (msgItemData.isRead) {
        //全部人已读，微信绿
        return "background: rgb(149,236,105)";
      } else {
        //有人未读，深蓝色
        return "background: #a3c3f6;";
      }
    },
    handlePaginationChange(val) {
      this.pageInfo.pageNow = val;
      this.getTableData();
    },
    preAddUserForGroup() {
      //1.获取privateTeamPageInfo
      this.getPrivateTeamPageInfo();
      //2.展开对话框
      this.notices.isAddMemberVisible = true;
    },
    load() {
      if (
          this.privateTeamPageInfo.totalSize ==
          this.privateTeamPageInfo.tableData.length
      ) {
        return;
      }
      this.privateTeamPageInfo.pageNow++;
    },
    getPrivateTeamPageInfo() {
      var that = this;
      let getPrivateTeamUrl =
          that.GLOBAL.networkSrc +
          "/getPrivateTeam?" +
          qs.stringify(that.privateTeamPageInfo);
      var config = {
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
          token: Cookies.get("token"),
        },
      };
      var serverResponse;
      axios
          .get(getPrivateTeamUrl, config)
          .then(function (result) {
            serverResponse = JSON.parse(result.request.response);
            console.log("Get Private Team Data:");
            console.log(serverResponse.data);
            if (serverResponse.code == 1) {
              //翻页，数组量累加
              that.privateTeamPageInfo.tableData.push(
                  ...serverResponse.data.tableData
              );
              console.log("Now Private Team Page Info: ");
              console.log(that.privateTeamPageInfo.tableData);
              //处理selectableIds：若创建团队，curRow中的userId不行；若添加好友入群，curRow中的team中成员的不行
              that.privateTeamPageInfo.selectableIds = [];
              if (that.currentRow.joinNum == 2) {
                //创建团队
                console.log(serverResponse.data.tableData);
                that.privateTeamPageInfo.tableData.forEach((item) => {
                  if (item.teamId != that.currentRow.teamId) {
                    that.privateTeamPageInfo.selectableIds.push(item.teamId);
                  }
                });
              } else {
                //添加好友入群
                that.privateTeamPageInfo.tableData.forEach((item) => {
                  if (that.subTeamIds.includes(item.teamId) == false) {
                    that.privateTeamPageInfo.selectableIds.push(item.teamId);
                  }
                });
              }
              console.log("Selectable Team:");
              console.log(that.privateTeamPageInfo.selectableIds);
              //刷新复选框
              that.$refs.multipleTable.clearSelection();
            } else {
              that.$message({
                type: "error",
                message: serverResponse.message,
                duration: 1500,
              });
              console.log(serverResponse.message);
            }
          })
          .catch((error) => {
            console.log("出错了，请联系后台管理人员" + error);
          });
    },
    handleSelectionChange(val) {
      //val是被选中的团队信息json行数据
      this.selectedTeamRow = val;
      console.log("Selected Team Row: ");
      console.log(this.selectedTeamRow);
    },
    addUserForGroup() {
      var that = this;
      //决定添加好友到好友列表中，有两种情况：1.新建群聊 2.添加好友入群
      //获取具体意图
      if (this.currentRow.joinNum == 2) {
        //1.新建群聊
        that.newTeamByTeam();
        return;
      }
      //2.拉人入群
      //2.1 制作请求
      var teamIds = [];
      for (var i = 0; i < that.selectedTeamRow.length; i++) {
        teamIds.push(that.selectedTeamRow[i].teamId);
      }
      let addMemberUrl =
          that.GLOBAL.networkSrc +
          "/addMemberByTeam/" +
          that.loginData.id +
          "/" +
          that.currentRow.teamId +
          "/" +
          teamIds.join(",");
      var config = {
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
          token: Cookies.get("token"),
        },
      };
      var serverResponse;
      axios
          .put(addMemberUrl, {}, config)
          .then(function (result) {
            serverResponse = JSON.parse(result.request.response);
            console.log("Get Response:");
            console.log(serverResponse.data);
            //2.通知结果
            if (serverResponse.code == 1) {
              that.$message({
                type: "success",
                message: "添加成功",
                duration: 1500,
              });
              //3.刷新群成员信息
              that.handleTableChange(that.currentRow);
              //4.关闭对话框
              that.notices.isAddMemberVisible = false;
            } else {
              that.$message({
                type: "error",
                message: serverResponse.message,
                duration: 1500,
              });
              console.log(serverResponse.message);
            }
          })
          .catch((error) => {
            console.log("出错了，请联系后台管理人员" + error);
          });
    },
    newTeamByTeam() {
      var that = this;
      //决定添加好友到好友列表中，有两种情况：1.新建群聊 2.添加好友入群
      //1.发送请求
      let newTeamByTeamUrl =
          that.GLOBAL.networkSrc + "/newTeamByTeam/" + that.loginData.id + "/";
      var teamIds = [];
      //包括当前行
      teamIds.push(that.currentRow.teamId);
      for (var i = 0; i < that.selectedTeamRow.length; i++) {
        teamIds.push(that.selectedTeamRow[i].teamId);
      }
      newTeamByTeamUrl += teamIds.join(",");
      var config = {
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
          token: Cookies.get("token"),
        },
      };
      var serverResponse;
      axios
          .put(newTeamByTeamUrl, {}, config)
          .then(function (result) {
            serverResponse = JSON.parse(result.request.response);
            console.log("Get Response:");
            console.log(serverResponse.data);
            //2.通知结果
            if (serverResponse.code == 1) {
              that.$message({
                type: "success",
                message: "添加成功",
                duration: 1500,
              });
              that.notices.isAddMemberVisible = false;
              that.getTableData();
            } else {
              that.$message({
                type: "error",
                message: serverResponse.message,
                duration: 1500,
              });
              console.log(serverResponse.message);
            }
          })
          .catch((error) => {
            console.log("出错了，请联系后台管理人员" + error);
          });
    },
    rowSelectable(row) {
      //判断是否已经被选中
      return this.privateTeamPageInfo.selectableIds.includes(row.teamId);
    },
    SendMessage() {
      var that = this;
      if (that.textArea == "" || that.textArea == "\n") {
        that.textArea = "";
        that.notices.isNullMessageSend = true;
        setTimeout(() => {
          that.notices.isNullMessageSend = false;
        }, 1300);
        return;
      }

      var sendMessage = {
        code: 4,
        sendUserId: that.loginData.id,
        teamId: that.currentRow.teamId,
        content: that.textArea,
      };
      console.log("Send Message: ");
      console.log(sendMessage);
      if (this.textArea === "") {
        return;
      } else {
        that.ws.send(JSON.stringify(sendMessage));
        this.textArea = "";
      }
    },
    KeyPressed(evt) {
      if (evt.ctrlKey && evt.keyCode === 13) {
        this.textArea += "\n";
      } else if (evt.keyCode === 13) {
        this.SendMessage();
      }
    },
    onContextmenu(item) {
      this.$contextmenu({
        items: [
          {
            label: "撤回",
            icon: "el-icon-delete",
            onClick: () => {
              this.withdrawMessage(item.messageId);
            },
          },
        ],
        event,
        zIndex: 3,
        minWidth: 60,
      });
    },
    withdrawMessage(msgId) {
      var that = this;
      //1.编写信息
      var withdrawMessage = {
        code: 3,
        sendUserId: that.loginData.id,
        username: that.loginData.username,
        messageId: msgId,
      };
      //2.发送请求
      this.ws.send(JSON.stringify(withdrawMessage));
    },
    clearPage() {
      this.$confirm("是否清空页面？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        // this.nowPageMessage.forEach((item) => {
        //   if (item.isSpecial == null) {
        //     this.ignoreMessageIds[item.teamId].push(item.id);
        //   }
        // });
        this.nowPageMessage = [];
        this.$message({
          type: "success",
          message: "页面已清空!",
          duration: 1200,
        });
      });
    },
    beforeDestroy() {
      //关闭WebSocket，标记为非异常关闭
      this.isErrorClose = false;
      this.ws.close();
    },
  },
  mounted() {
  },
};
</script>

<style scope>
.MainBox {
  margin: 0 200px;
  /* background-color: gray; */
}

.search-input .el-input__inner {
  border-radius: 80px;
}

.last-chat-time {
  float: right;
  font-size: 13px;
  color: #999;
}

.chat-content {
  width: 100%;
  height: 400px;
  padding: 20px;
  background-color: rgb(245, 245, 245);
  overflow: auto;
}

.word {
  display: flex;
  margin-bottom: 20px;
}

.word img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.word .info {
  margin-left: 10px;
}

.word .info .time {
  display: flex;
  justify-content: flex-start;
  font-size: 12px;
  color: rgba(51, 51, 51, 0.8);
  margin: 0;
  height: 20px;
  line-height: 20px;
}

.word .info .info-content {
  display: flex;
  justify-content: flex-start;
  overflow: auto;
  max-width: 70%;
  max-height: fit-content;
  padding: 10px;
  font-size: 14px;
  background: #fff;
  position: relative;
  margin-top: 8px;
}

.word .info .info-content::before {
  position: absolute;
  left: -8px;
  top: 8px;
  content: "";
  border-right: 10px solid #fff;
  border-top: 8px solid transparent;
  border-bottom: 8px solid transparent;
}

.word-my {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}

.word-my img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.word-my .info {
  width: 90%;
  margin-left: 10px;
  text-align: right;
}

.word-my .info .time {
  font-size: 12px;
  color: rgba(51, 51, 51, 0.8);
  margin: 0;
  height: 20px;
  line-height: 20px;
  margin-top: -5px;
  margin-right: 10px;
}

.word-my .info .info-content {
  max-width: 70%;
  padding: 10px;
  font-size: 14px;
  float: right;
  margin-right: 10px;
  position: relative;
  margin-top: 8px;
  /* background: #a3c3f6; */
  text-align: left;
}

.word-my .info .info-content::after {
  position: absolute;
  right: -8px;
  top: 8px;
  content: "";
  /* border-left: 10px solid #a3c3f6; */
  border-top: 8px solid transparent;
  border-bottom: 8px solid transparent;
}
</style>
