<template>
  <v-row justify="center">
    <v-container>
      <v-list two-line>
        <div v-for="(item, index) in mySeats" :key="item.id">
          <v-list-item @click.stop="$router.push(`room/${item.id}`)">
            <v-col cols="11" md="10" sm="8">
              <v-list-item-content>
                <v-list-item-title
                  v-text="
                    roomName + ' # ' + item.id
                  "
                />
                <!-- <v-list-item-subtitle v-text="item.myState" /> -->
                <!-- <v-list-item-subtitle v-text="item.content" /> -->
              </v-list-item-content>
            </v-col>
            <v-col cols="2" sm="1">
              <v-btn md="4" color="orange" @click.stop="deleteSeat(item.id)">
                删除
              </v-btn>
            </v-col>
          </v-list-item>

          <v-divider v-if="index + 1 < mySeats.length" :key="'divider' + index" />
        </div>
      </v-list>
    </v-container>
    <v-btn
      class="mx-2"
      style="position: fixed; bottom: 5%; right: 5%;"
      fab
      dark
      color="indigo"
      @click="addSeat()"
    >
      <v-icon dark>
        mdi-plus
      </v-icon>
    </v-btn>
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
import { getSeatsByRoomId, deleteSeat, addSeat } from '../../services/seat.service';
import renderTime from '../../utils/time';
import log from '../../utils/log';

export default {
  data: () => ({
    roomName: '',
    seats: [],
    snackbar: '',
    snackbarColor: '',
    snackbarText: '',
    snackbarTimeout: 2000,
  }),
  computed: {
    ...mapState(['user', 'rooms']),
    ...mapGetters(['countRooms']),
    countSeats: () => Object.keys(this.seats).length,
    barTitle: () => `${this.roomName}: ${this.countSeats} seats`,
    mySeats() {
      const items = [];
      this.seats.forEach((element) => {
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
          myState,
        });
      });
      return items;
    },
  },

  created() {
    this.roomName = this.$route.query.roomName;
    this.fetchData();
    this.$store.commit('SET_BAR_TITLE', `管理座位`);
  },
  methods: {
    showSnackbar(type, content) {
      this.snackbarColor = type;
      this.snackbarText = content;
      this.snackbar = true;
    },

    fetchData() {
      log.info('room id:', this.$route);
      getSeatsByRoomId(this.$route.params.id)
        .then((seats) => {
          this.seats = seats;
          log.info(this.seats);
        })
        .catch((err) => {
          log.info(err);
        });
    },

    addSeat() {
      addSeat(this.$route.params.id)
        .then((data) => {
          log.info(data);
          this.showSnackbar('success', '添加成功');
          this.fetchData();
        })
        .catch((err) => {
          this.showSnackbar('error', err);
          log.info(err);
        });
    },

    deleteSeat(seatId) {
      deleteSeat(seatId)
        .then((data) => {
          log.info(data);
          this.showSnackbar('success', '删除成功');
          this.fetchData();
        })
        .catch((err) => {
          log.info(err);
        });
    },
  },
};
</script>
