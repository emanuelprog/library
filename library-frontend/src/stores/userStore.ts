import { defineStore } from 'pinia';
import { ref } from 'vue';
import type { UserType } from 'src/types/userType';

export const useUserStore = defineStore('user', () => {
  const selectedUser = ref<UserType | null>(null);

  function setSelectedUser(user: UserType | null) {
    selectedUser.value = user;
  }

  function clear() {
    selectedUser.value = null;
  }

  return {
    selectedUser,
    setSelectedUser,
    clear
  };
}, {
  persist: true
});
