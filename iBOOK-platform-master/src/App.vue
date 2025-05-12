<template>
  <v-app>
    <NavigationDrawers />
    <v-content>
      <router-view />
    </v-content>
  </v-app>
</template>

<script>
import NavigationDrawers from './components/common/NavigationDrawers.vue';

export default {
  name: 'App',

  components: {
    NavigationDrawers,
  },

  data: () => ({
    //
  }),
};
</script>
