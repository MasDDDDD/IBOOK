<template>
  <v-row justify="center">
    <v-container>
      <v-list two-line>
        <div v-for="(item, index) in rooms" :key="item.id">
          <v-list-item @click.stop="$router.push({path: `/admin/room/${item.id}`, query:{roomName: item.buildingNum + item.classRoomNum}})">
            <v-col cols="6" md="7" sm="6">
              <v-list-item-content>
                <v-list-item-title
                  v-text="
                    item.buildingNum +
                      item.classRoomNum +
                      `  ———— Open Time: ` +
                      item.startTime +
                      `:00 ~ ` +
                      item.endTime +
                      `:00`
                  "
                />
                <v-list-item-subtitle v-text="item.state == 1 ? `available` : `closed`" />
                <!-- <v-list-item-subtitle v-text="item.content" /> -->
              </v-list-item-content>
            </v-col>
            <v-col cols="1">
              <v-btn md="2" color="orange" @click.stop="deleteRoom(item.id)">
                删除
              </v-btn>
            </v-col>
            <v-col cols="2">
              <v-btn md="2" color="success" @click.stop="clickEditRoom(item)">
                调整时间
              </v-btn>
            </v-col>
            <v-col cols="2">
              <v-btn md="2" color="primary" @click.stop="editRoomState(item)">
                切换状态
              </v-btn>
            </v-col>
          </v-list-item>

          <v-divider v-if="index + 1 < rooms.length" :key="'divider' + index" />
        </div>
      </v-list>
    </v-container>
    <v-btn
      class="mx-2"
      style="position: fixed; bottom: 5%; right: 5%;"
      fab
      dark
      color="indigo"
      @click="addDialog = true"
    >
      <v-icon dark>
        mdi-plus
      </v-icon>
    </v-btn>
    <v-dialog v-model="addDialog" transition="dialog-bottom-transition" max-width="600px">
      <v-card>
        <v-card-title>
          <span class="text-h5">新建自习室</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12" sm="6">
                <v-text-field
                  v-model="newBuildingNum"
                  label="教学楼"
                  hint="e.g. HGX JA"
                  required
                />
              </v-col>
              <v-col cols="12" sm="6">
                <v-text-field
                  v-model="newClassRoomNum"
                  label="教室号"
                  hint="e.g. 109 607"
                  required
                />
              </v-col>
              <v-col cols="12" sm="6">
                <v-text-field v-model="newStartTime" label="StartTime" required />
              </v-col>
              <v-col cols="12" sm="6">
                <v-text-field v-model="newEndTime" label="EndTime" required />
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn color="blue darken-1" text @click="addDialog = false">
            Close
          </v-btn>
          <v-btn color="blue darken-1" text @click="addRoom()">
            Save
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-dialog v-model="editDialog" transition="dialog-bottom-transition" max-width="600px">
      <v-card>
        <v-card-title>
          <span class="text-h5" v-text="`编辑自习室: `+ roomItem.buildingNum + roomItem.classRoomNum" />
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12" sm="6">
                <v-text-field v-model="editStartTime" label="StartTime" required />
              </v-col>
              <v-col cols="12" sm="6">
                <v-text-field v-model="editEndTime" label="EndTime" required />
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn color="blue darken-1" text @click="editDialog = false">
            Close
          </v-btn>
          <v-btn color="blue darken-1" text @click="editRoomTime()">
            Save
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-snackbar v-model="snackbar" :color="snackbarColor" :timeout="snackbarTimeout">
      {{ snackbarText }}
      <v-btn dark text @click="snackbar = false">
        Close
      </v-btn>
    </v-snackbar>
  </v-row>
</template>

<script>
import { mapGetters, mapState } from 'vuex';
import {
  getRooms,
  deleteRoom,
  editRoomTime,
  editRoomState,
  addRoom,
} from '../../services/room.service';
import renderTime from '../../utils/time';
import log from '../../utils/log';
import { doNothing, formatDate } from '../../utils/common';

export default {
  data: () => ({
    addDialog: false,
    editDialog: false,
    roomItem: '',
    newBuildingNum: '',
    newClassRoomNum: '',
    newStartTime: '',
    newEndTime: '',
    editRoomId: '',
    editStartTime: '',
    editEndTime: '',
    editState: '',
    snackbar: '',
    snackbarColor: '',
    snackbarText: '',
    snackbarTimeout: 2000,
    // titleRules: [(value) => !value || value.length <= 30 || '请确保标题不超过30字'],
    // contentRules: [(value) => !value || value.length <= 128 || '请确保详情不超过128字'],
  }),
  computed: {
    ...mapState(['user', 'rooms']),
    ...mapGetters(['countRooms']),
  },

  created() {
    this.$store.commit('SET_BAR_TITLE', '管理自习室');
    this.fetchData();
    this.date = formatDate(new Date());
  },
  methods: {
    showSnackbar(type, content) {
      this.snackbarColor = type;
      this.snackbarText = content;
      this.snackbar = true;
    },

    clearVModel() {
      this.newBuildingNum = '';
      this.newClassRoomNum = '';
      this.newStartTime = '';
      this.newEndTime = '';
    },

    fetchData() {
      getRooms()
        .then((rooms) => {
          this.$store.commit('SET_ROOMS', rooms);
          log.info(this.rooms);
        })
        .catch((err) => {
          log.info(err);
        });
    },

    clickEditRoom(roomItem) {
      this.editDialog = true;
      this.roomItem = roomItem;
      this.editRoomId = roomItem.id;
      this.editState = roomItem.state == 1 ? `available` : `closed`;
      this.editStartTime = roomItem.startTime;
      this.editEndTime = roomItem.endTime;
    },

    addRoom() {
      addRoom(this.newBuildingNum, this.newClassRoomNum, this.newStartTime, this.newEndTime)
        .then((data) => {
          log.info(data);
          this.showSnackbar('success', '添加成功');
          this.fetchData();
          this.addDialog = false;
          this.clearVModel();
        })
        .catch((err) => {
          this.showSnackbar('error', err);
          log.info(err);
        });
    },

    deleteRoom(roomId) {
      deleteRoom(roomId)
        .then((data) => {
          log.info(data);
          this.showSnackbar('success', '删除成功');
          this.fetchData();
        })
        .catch((err) => {
          log.info(err);
        });
    },

    editRoomState(item) {
      editRoomState(item.id, 1 - item.state)
        .then((data) => {
          log.info(data);
          this.fetchData();
        })
        .catch((err) => {
          log.info(err);
        });
    },

    editRoomTime() {
      editRoomTime(this.editRoomId, this.editStartTime, this.editEndTime)
        .then((data) => {
          this.fetchData();
          this.showSnackbar('success', '修改成功');
          this.editDialog = false;
          log.info(data);
        })
        .catch((err) => {
          log.info(err);
        });
    },
  },
};
</script>
