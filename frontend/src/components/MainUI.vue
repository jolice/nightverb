<!--suppress HtmlUnknownTag -->
<template>


  <div v-if="!allHidden"
       style="margin-top: 20%; display: flex; align-items: center; flex-direction: column; justify-content: center;">
    <div>
      Enter YouTube URL
    </div>
    <el-form style="margin-top: 10px">


      <div>
        <el-input :disabled="progress" placeholder="     https://www.youtube.com/watch?v=kJQP7kiw5Fk"
                  style="width: 400px" v-model="youtubeUrl">
        </el-input>
      </div>

      <div style="margin-top: 10px">
        <el-button :disabled="progress || !urlReady" type="primary" style="margin-left: 20px;"
                   @click="createTask('nightcore')">
          Nightcore
        </el-button>
        <el-button :disabled="progress || !urlReady" type="primary" style="margin-left: 165px;"
                   @click="createTask('reverb')">
          Reverb
        </el-button>
      </div>

      <div v-loading="progress" :element-loading-text="prettyStatus">
        <div v-if="fileUid !== undefined && progress === false">
          <audio style="width: 400px; margin-top: 20px; " controls :src="url">
            Your browser does not support the
            <code>audio</code> element.
          </audio>
          <i class="el-icon-download" @click="downloadCurrent" style="font-size: 3.1em;"></i>
        </div>
      </div>



    </el-form>



    <el-collapse style="margin-top: 70px">
      <el-collapse-item title="History" name="1">
        <el-table
            v-if="history.length > 0"
            :data="history">
          <el-table-column
              label="File name"
              prop="fileName"
              width="180">

            <template slot-scope="scope">
              <a href="javascript:void(null);" @click="download(scope.row.fileId)">{{ transliterate(scope.row.fileName) }}</a>
            </template>

          </el-table-column>
          <el-table-column prop="Audio" label="Date" width="450">

            <template slot-scope="scope">
              <audio style="width: 400px; margin-top: 20px; " controls
                     :src="urlId(scope.row.fileId)">
                Your browser does not support the
                <code>audio</code> element.
              </audio>
            </template>


          </el-table-column>

        </el-table>
      </el-collapse-item>
    </el-collapse>


  </div>

</template>

<script>
import SockJS from 'sockjs-client'
import Stomp from 'webstomp-client'
import axios from "axios";
import FileDownload from "js-file-download";


const sessionUserIdAttr = 'SessionUserId';

export default {
  name: 'MainUI',
  props: {
    msg: String
  },
  mounted() {
    document.querySelectorAll('audio').forEach(el => el.stop());
    //localStorage.clear(); //
    let sessionId = localStorage.getItem(sessionUserIdAttr);
    console.log('Session user id: ' + sessionId);
    const channel = new BroadcastChannel('tab');
    channel.postMessage('another-tab');
    let window = this;
    this.fetchHistory();
    channel.addEventListener('message', (msg) => {
      // eslint-disable-next-line no-constant-condition
      if (true) {
        return;
      }
      if (msg.data === 'another-tab') {
        this.allHidden = true;
        this.$alert('Only one tab of the service can be open at the same time', 'Sorry', {
          showConfirmButton: false,
          showClose: false,
          showCancelButton: false,
          callback: action => {
            this.$message({
              type: 'info',
              message: `action: ${action}`
            });
          }
        });
      }
    });
  },
  computed: {
    url() {
      return this.urlId(this.fileUid)
    },
    urlReady() {
      return this.youtubeUrl !== undefined;
    },

    prettyStatus() {
      if ('processing_video' === this.status) {
        return 'Processing video'
      } else if ('processing_audio' === this.status) {
        return 'Processing audio'
      } else if ('success' === this.status) {
        return 'Almost here...'
      }
      return 'Waiting'
    }
  },
  methods: {
      urlId(id) {
        return process.env.VUE_APP_API_URL + '/stream/' + id
      },
    transliterate(word){
  var answer = ""
      , a = {};

  a["Ё"]="YO";a["Й"]="I";a["Ц"]="TS";a["У"]="U";a["К"]="K";a["Е"]="E";a["Н"]="N";a["Г"]="G";a["Ш"]="SH";a["Щ"]="SCH";a["З"]="Z";a["Х"]="H";a["Ъ"]="'";
  a["ё"]="yo";a["й"]="i";a["ц"]="ts";a["у"]="u";a["к"]="k";a["е"]="e";a["н"]="n";a["г"]="g";a["ш"]="sh";a["щ"]="sch";a["з"]="z";a["х"]="h";a["ъ"]="'";
  a["Ф"]="F";a["Ы"]="I";a["В"]="V";a["А"]="a";a["П"]="P";a["Р"]="R";a["О"]="O";a["Л"]="L";a["Д"]="D";a["Ж"]="ZH";a["Э"]="E";
  a["ф"]="f";a["ы"]="i";a["в"]="v";a["а"]="a";a["п"]="p";a["р"]="r";a["о"]="o";a["л"]="l";a["д"]="d";a["ж"]="zh";a["э"]="e";
  a["Я"]="Ya";a["Ч"]="CH";a["С"]="S";a["М"]="M";a["И"]="I";a["Т"]="T";a["Ь"]="'";a["Б"]="B";a["Ю"]="YU";
  a["я"]="ya";a["ч"]="ch";a["с"]="s";a["м"]="m";a["и"]="i";a["т"]="t";a["ь"]="'";a["б"]="b";a["ю"]="yu";

  for (var i in word){
    // eslint-disable-next-line no-prototype-builtins
    if (word.hasOwnProperty(i)) {
      if (a[word[i]] === undefined){
        answer += word[i];
      } else {
        answer += a[word[i]];
      }
    }
  }
  return answer;
},
    downloadCurrent: function () {
      this.download(this.fileUid);
    },
    download: function (fileId) {
      axios.get('/file/download/' + fileId, {responseType: 'blob'}).then((response) => {
        let filename = response.headers['content-disposition'].split('filename=')[1].split('.')[0];
        let extension = response.headers['content-disposition'].split('.')[1].split(';')[0];
        FileDownload(response.data, `${filename}.${extension}`);
      }).catch((response) => {
        console.error("Could not Download file from the backend.", response);
      });

    },
    websocketConnect: function (op) {
      let that = this;
      this.socket = new SockJS(process.env.VUE_APP_API_URL + '/ws');
      this.stompClient = Stomp.over(this.socket);
      this.stompClient.connect({}, function (frame) {
        that.wsSessionId = /\/([^/]+)\/websocket/.exec(that.socket._transport.url)[1];
        console.log("WS session id: " + that.wsSessionId)
        that.stompClient.subscribe('/user/queue/messages', that.websocketEventHandler);
        op();
      });
    },
    fetchHistory() {
      let sessionId = localStorage.getItem(sessionUserIdAttr);
      if (sessionId === null) {
        return;
      }
      axios.get("task/history", {
        headers: {
          SessionUserId: sessionId
        }
      }).then((rsp) => {
        this.history = rsp.data;
      });
    },
    websocketEventHandler: function (msgOut) {
      var json = JSON.parse(msgOut.body);
      let fileUid = json['fileUid'];
      // Completed - received fileUid
      if (fileUid !== undefined) {
        this.fileUid = fileUid;
        console.log('Completed');
        console.log('Set fileUid = ' + this.fileUid);
        this.progress = false;
        this.stompClient.disconnect(() => {
          console.log('Disconnected from WS')
        });
        this.fetchHistory();
      }
      let status = json['status'];
      if (status !== undefined) {
        this.status = status;
      }
    },
    obtainToken(op) {
      return axios.post("session/start", {})
          .then((rsp) => {
            let token = rsp.data['id'];
            console.log('Obtain token: ' + token)
            localStorage.setItem(sessionUserIdAttr, token);
            op();
          }).catch((err) => {
            console.log('Failed to obtain token: ' + err)
          })
    },
    createTask: function (type) {
      if (!this.youtubeUrl.match('^((?:https?:)?\\/\\/)?((?:www|m)\\.)?((?:youtube(-nocookie)?\\.com|youtu.be))(\\/(?:[\\w\\-]+\\?v=|embed\\/|v\\/)?)([\\w\\-]+)(\\S+)?$')) {
        this.$message({
          showClose: true,
          message: 'Enter valid youtube URL',
          type: 'error'
        });
        return;
      }
      this.preCreateTask(type);
    },
    preCreateTask(type) {
      let createTaskOp = () => {
        console.log('Creating task: ' + this.youtubeUrl + ', connecting to WS first');
        this.websocketConnect(() => {
          console.log('POST task/create')
          this.createTaskApi(type);
        });
      };
      let sessionUserId = localStorage.getItem(sessionUserIdAttr);
      if (sessionUserId === null) {
        this.obtainToken(() => createTaskOp());
      } else {
        createTaskOp();
      }
    },
    createTaskApi(type) {
      let sessionId = localStorage.getItem(sessionUserIdAttr);
      if (sessionId === null) {
        console.log('SessionUserID not set')
        return;
      }
      console.log('Session ID: ' + sessionId);
      axios.post("task/create", {
        youtubeUrl: this.youtubeUrl,
        type: type,
        sessionId: this.wsSessionId
      }, {
        headers: {
          SessionUserId: sessionId
        }
      }).then((rsp) => {
        console.log(rsp)
        if (rsp.status === 200) {
          this.fileUid = undefined;
          this.progress = true;
        } else {
          this.$message({
            showClose: true,
            message: 'Bad status: ' + rsp.status,
            type: 'error'
          });
        }
      }).catch(() => {
        this.$message({
          showClose: true,
          message: 'Oops! An error has occurred',
          type: 'error'
        });
      });
    }
  },
  data() {
    return {
      history: [],
      allHidden: false,
      wsSessionId: null,
      youtubeUrl: undefined,
      fileUid: undefined,
      progress: false,
      status: '',
      socket: undefined,
      stompClient: undefined,
    }
  }
}
</script>

<style>

.el-header {
  background-color: #B3C0D1;
  color: #333;
  line-height: 60px;
}

.el-aside {
  color: #333;
}

img.resize {
  max-width: 13%;
  max-height: 13%;
}

.grayout {
  opacity: 0.3; /* Real browsers */
  filter: alpha(opacity=60); /* MSIE */
}

</style>
