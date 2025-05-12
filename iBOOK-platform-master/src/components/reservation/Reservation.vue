<template>
  <v-row justify="center">
    <v-container>
      <v-list two-line>
        <div v-for="(item, index) in myReservations" :key="item.id">
          <v-list-item @click.stop="$router.push(`room/${item.id}`)">
            <v-col cols="9" md="9" sm="6">
              <v-list-item-content>
                <v-list-item-title
                  v-text="
                    item.buildingNum +
                      item.classRoomNum +
                      ` # ` +
                      item.seatId +
                      ` —— 开始: ` +
                      item.startTime.slice(5, -3) +
                      ` 时长: ` +
                      item.bookingPeriod + 
                      `h`
                  "
                />
                <v-list-item-subtitle v-text="item.myState + ` ` + item.createdTime" />
                <!-- <v-list-item-subtitle v-text="item.content" /> -->
              </v-list-item-content>
            </v-col>
            <v-col cols="1">
              <v-btn color="orange" @click.stop="signIn(item.id)">
                签到
              </v-btn>
            </v-col>
            <v-col cols="1">
              <v-btn color="info" @click.stop="signOut(item.id)">
                签退
              </v-btn>
            </v-col>
            <v-col cols="1">
              <v-btn color="success" @click.stop="cancel(item.id)">
                取消
              </v-btn>
            </v-col>
          </v-list-item>

          <v-divider v-if="index + 1 < myReservations.length" :key="'divider' + index" />
        </div>
      </v-list>
    </v-container>
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
import { getMyReservation, signIn, signOut, cancel } from '../../services/reservation.service';
import renderTime from '../../utils/time';
import renderBeginTime from '../../utils/time';
import log from '../../utils/log';

export default {
  data: () => ({
    snackbar: '',
    snackbarColor: '',
    snackbarText: '',
    snackbarTimeout: 2000,
  }),
  computed: {
    ...mapState(['user', 'reservations']),
    ...mapGetters(['countReservations']),
    myReservations() {
      const items = [];
      this.reservations.forEach((element) => {
        const createdTime = renderTime(element.opTime);
        const myStartTime = renderBeginTime(element.startTime);
        let myState = '';
        switch (element.state) {
          case -1:
            myState = '抢座待签到';
            break;
          case 0:
            myState = '待签到';
            break;
          case 1:
            myState = '已签到';
            break;
          case 2:
            myState = '已取消';
            break;
          case 3:
            myState = '违约';
            break;
          case 4:
            myState = '已完成';
            break;
        }
        items.push({
          ...element,
          createdTime,
          myStartTime,
          myState,
        });
      });
      return items;
    },
  },

  created() {
    this.$store.commit('SET_BAR_TITLE', '我的预定');
    this.fetchData();
  },
  methods: {
    showSnackbar(type, content) {
      this.snackbarColor = type;
      this.snackbarText = content;
      this.snackbar = true;
    },

    fetchData() {
      getMyReservation()
        .then((reservations) => {
          this.$store.commit('SET_RESERVATIONS', reservations);
          log.info(this.reservations);
        })
        .catch((err) => {
          log.info(err);
        });
    },

    signIn(id) {
      signIn(id)
        .then((data) => {
          this.fetchData();
          this.showSnackbar('success', '签到成功');
          log.info(data);
        })
        .catch((err) => {
          log.info(err);
          this.showSnackbar('error', err.response.data.message);
        });
    },

    signOut(id) {
      signOut(id)
        .then((data) => {
          this.fetchData();
          this.showSnackbar('success', '签退成功');
          log.info(data);
        })
        .catch((err) => {
          log.info(err);
          this.showSnackbar('error', err.response.data.message);
        });
    },

    cancel(id) {
      cancel(id)
        .then((data) => {
          this.fetchData();
          this.showSnackbar('success', '预约已取消');
          log.info(data);
        })
        .catch((err) => {
          this.showSnackbar('error', err.response.data.message);
          log.info(err);
        });
    },
  },
};
</script>
