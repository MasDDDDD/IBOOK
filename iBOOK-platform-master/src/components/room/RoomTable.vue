<template>
  <v-row justify="center">
    <v-container>
      <!-- <v-menu
        ref="menu"
        v-model="datePicker"
        :close-on-content-click="false"
        :return-value.sync="date"
        transition="scale-transition"
        offset-y
        min-width="auto"
      >
        <template v-slot:activator="{ on, attrs }">
          <v-combobox
            v-model="date"
            chips
            small-chips
            label="请选择一个日期"
            prepend-icon="mdi-calendar"
            readonly
            v-bind="attrs"
            v-on="on"
          />
        </template>
        <v-date-picker
          v-model="date"
          no-title
          scrollable
        >
          <v-spacer />
          <v-btn
            text
            color="primary"
            @click="datePicker = false"
          >
            Cancel
          </v-btn>
          <v-btn
            text
            color="primary"
            @click="$refs.menu.save(date)"
          >
            OK
          </v-btn>
        </v-date-picker>
      </v-menu> -->
      <v-list two-line>
        <div v-for="(item, index) in rooms" :key="item.id">
          <v-list-item @click.stop="$router.push(`room/${item.id}`)">
            <v-list-item-content>
              <v-list-item-title v-text="item.buildingNum + item.classRoomNum + `  ———— Open Time: ` + item.startTime + `:00 ~ ` + item.endTime + `:00`" />
              <v-list-item-subtitle
                v-text="item.state==1?`available`:`closed`"
              />
              <!-- <v-list-item-subtitle v-text="item.content" /> -->
            </v-list-item-content>
          </v-list-item>

          <v-divider v-if="index + 1 < rooms.length" :key="'divider' + index" />
        </div>
      </v-list>
    </v-container>
  </v-row>
</template>

<script>
import { mapGetters, mapState } from 'vuex';
import { getRooms } from '../../services/room.service';
import renderTime from '../../utils/time';
import log from '../../utils/log';
import { doNothing, formatDate } from '../../utils/common';

export default {
  data: () => ({
    showDialog: false,
    newTitle: '',
    newContent: '',
    // date: '',
    // datePicker: false,
    // rooms: [
    //   {
    //     id: 'GHX401',
    //     status: 'available',
    //   },
    //   {
    //     id: 'GHX403',
    //     status: 'closed',
    //   },
    //   {
    //     id: 'GHX509',
    //     status: 'full',
    //   },
    // ],
    titleRules: [(value) => !value || value.length <= 30 || '请确保标题不超过30字'],
    contentRules: [(value) => !value || value.length <= 128 || '请确保详情不超过128字'],
  }),
  computed: {
    ...mapState(['user', 'rooms']),
    ...mapGetters(['countRooms']),
  },

  created() {
    this.$store.commit('SET_BAR_TITLE', '自习室');
    this.fetchData();
    this.date = formatDate(new Date());
  },
  methods: {
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
  },
};
</script>
