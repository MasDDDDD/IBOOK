<template>
  <v-row justify="center">
    <v-container>
      <v-card>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col cols="12" sm="6">
                <v-text-field v-model="stuNum" label="请输入学号" required />
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn color="blue darken-1" text @click="searchUserBooks()">
            搜索
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-container>
    <v-container>
      <v-list v-if="items.length > 0" two-line>
        <div v-for="item in items" :key="item.id">
          <v-list-item>
            <v-col cols="8" md="8" sm="6">
              <v-list-item-content>
                <v-list-item-title
                  v-text="
                    item.stuNum + ` 预约了` +
                      item.buildingNum +
                      item.classRoomNum +
                      `从` + item.startTime +
                      `开始的` +
                      item.bookingPeriod +
                      `小时，已违约`
                  "
                />
                <!-- <v-list-item-subtitle v-text="item.content" /> -->
              </v-list-item-content>
            </v-col>
            <v-col cols="1">
              <v-btn md="4" color="orange" @click.stop="deleteBook(item.id)">
                删除该条记录
              </v-btn>
            </v-col>
          </v-list-item>
        </div>
      </v-list>
    </v-container>
    <v-dialog v-model="showDialog" persistent max-width="500">
      <v-card>
        <v-card-title class="headline">
          删除成功
        </v-card-title>

        <v-card-actions>
          <v-spacer />
          <v-btn color="green darken-1" text @click="showDialog = false">
            好
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
import { mapGetters, mapState } from 'vuex';
import { getUserBooks } from '../../services/booking.service';
import log from '../../utils/log';
import { doNothing, formatDate } from '../../utils/common';

export default {
  data: () => ({
    items: [],
    stuNum: '',
    showDialog: false,
  }),
  computed: {},

  created() {
    this.$store.commit('SET_BAR_TITLE', '管理违约记录');
    // this.fetchData();
    this.date = formatDate(new Date());
  },
  methods: {
    searchUserBooks() {
      getUserBooks(this.stuNum).then((data) => {
        data.forEach((d) => {
          if (d.state === 3) {
            this.items.push(d);
          }
        });
      });
    },
    deleteBook(id) {
      for (let i = 0; i < this.items.length; i++) {
        if (this.items[i].id === id) {
          this.items.splice(i, 1);
          this.showDialog = true;
          break;
        }
      }
    },
  },
};
</script>
