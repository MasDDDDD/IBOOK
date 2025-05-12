<template>
  <div>
    <v-navigation-drawer
      v-model="showMenu"
      src="../../assets/bg-2.jpg"
      dark
      app
      fixed
    >
      <v-list dense nav>
        <div v-if="!userLoggedIn">
          <v-list-item>
            <v-list-item-content>
              未登录
            </v-list-item-content>
          </v-list-item>
          <v-list-item link>
            <v-list-item-icon>
              <v-icon>mdi-alert</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>请登陆后使用</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </div>
        <div v-else-if="user.identity == 'normal'">
          <v-list-item two-line>
            <v-list-item-avatar>
              <img src="../../assets/avatar.jpg">
            </v-list-item-avatar>
            <v-list-item-content>
              <v-list-item-title>{{ user.name + ' (' + user.stuNum + ')' }}</v-list-item-title>
              <v-list-item-subtitle>{{ user.identity }}</v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>

          <v-divider />

          <v-list-item link @click="toDirection('/room')">
            <v-list-item-icon>
              <v-icon>mdi-table-chair</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>查看自习室</v-list-item-title>
            </v-list-item-content>
            <v-list-item-content>
              <v-list-item-title>
                {{ countRooms }}
              </v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item link @click="toDirection('/reservation')">
            <v-list-item-icon>
              <v-icon>mdi-chart-histogram</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>我的预定</v-list-item-title>
            </v-list-item-content>
            <v-list-item-content>
              <v-list-item-title>
                {{ countReservations }}
              </v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <!-- <v-list-item link @click="toDirection('/profile')">
            <v-list-item-icon>
              <v-icon>mdi-shield-account</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>个人信息</v-list-item-title>
            </v-list-item-content>
          </v-list-item> -->
        </div>
        <div v-else>
          <v-list-item two-line>
            <v-list-item-avatar>
              <img src="../../assets/avatar.jpg">
            </v-list-item-avatar>
            <v-list-item-content>
              <v-list-item-title>{{ user.name + ' (' + user.stuNum + ')' }}</v-list-item-title>
              <v-list-item-subtitle>{{ user.identity }}</v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>

          <v-divider />

          <v-list-item link :to="`/admin/room`">
            <v-list-item-icon>
              <v-icon>mdi-table-chair</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>管理自习室</v-list-item-title>
            </v-list-item-content>
            <v-list-item-content>
              <v-list-item-title>
                {{ countRooms }}
              </v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item link :to="`/admin/user`">
            <v-list-item-icon>
              <v-icon>mdi-table-chair</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>管理违约记录</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <!-- <v-list-item link @click="toDirection('/reservation')">
            <v-list-item-icon>
              <v-icon>mdi-chart-histogram</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>管理预约</v-list-item-title>
            </v-list-item-content>
            <v-list-item-content>
              <v-list-item-title>
                {{ countMessages }}
              </v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item link @click="toDirection('/room')">
            <v-list-item-icon>
              <v-icon>mdi-table-chair</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>管理学生</v-list-item-title>
            </v-list-item-content>
            <v-list-item-content>
              <v-list-item-title>
                {{ countRooms }}
              </v-list-item-title>
            </v-list-item-content>
          </v-list-item> -->
        </div>
      </v-list>

      <template v-slot:append>
        <div class="pa-2">
          <v-btn block @click.stop="changeState">
            {{ status }}
          </v-btn>
        </div>
      </template>
    </v-navigation-drawer>

    <v-app-bar
      app
      fixed
      dark
      src="../../assets/bg-2.jpg"
      class="py-0"
    >
      <v-app-bar-nav-icon @click.stop="showMenuFun" />
      <v-toolbar-title>{{ barTitle }}</v-toolbar-title>
      <v-spacer />
    </v-app-bar>
  </div>
</template>

<script>
import { mapGetters, mapState } from 'vuex';
// import log from '../../utils/log';

export default {
  data() {
    return {
      showMenu: false,
    };
  },
  computed: {
    ...mapState(['barTitle', 'user']),
    ...mapGetters(['userLoggedIn', 'countRooms', 'countReservations']),
    status() {
      return this.userLoggedIn ? '登出' : '登录';
    },
  },
  methods: {
    showMenuFun() {
      this.showMenu = !this.showMenu;
    },
    doNothing() {},
    toDirection(dir) {
      if (this.$route.path === dir) {
        this.showMenu = false;
      } else {
        this.$router.push(dir);
      }
    },
    changeState() {
      if (this.userLoggedIn) {
        this.$store.commit('LOGOUT');
        this.toDirection('/');
      } else {
        this.toDirection('/login');
      }
    },
  },
};
</script>
