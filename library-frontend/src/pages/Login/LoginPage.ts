import axios from 'axios';
import { login, register } from 'src/services/authService';
import { notifyError, notifyWarning, notifySuccess } from 'src/services/messageService';
import { useUserStore } from 'src/stores/userStore';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

export function useLoginPage() {
  const username = ref('');
  const password = ref('');
  const submitted = ref(false);
  const loading = ref(false);
  const showPassword = ref(false);
  const isRegistering = ref(false);

  const userStore = useUserStore();
  const router = useRouter();

  async function handleLogin() {
    submitted.value = true;

    if (!username.value || !password.value) return;

    loading.value = true;

    try {
      const [userRes] = await Promise.all([
        login(username.value, password.value),
        sleep(500)
      ]);
      userStore.setSelectedUser(userRes);
      await router.push('/home');
    } catch (error) {
      handleError(error, 'Erro ao autenticar.');
    } finally {
      loading.value = false;
      submitted.value = false;
    }
  }

  async function handleRegister() {
    submitted.value = true;

    if (!username.value || !password.value) return;

    loading.value = true;

    await sleep(500);

    try {
      await register(username.value, password.value);
      notifySuccess('Usuário registrado com sucesso!');

      isRegistering.value = false;

      username.value = '';
      password.value = '';
    } catch (error) {
      handleError(error, 'Erro ao registrar usuário.');
    } finally {
      submitted.value = false;
      loading.value = false;
    }
  }

  function handleError(error: unknown, defaultMsg: string) {
    let errorMessage = defaultMsg;
    if (axios.isAxiosError(error) && error.response?.data?.message) {
      errorMessage = error.response.data.message;
    }
    if (axios.isAxiosError(error) && error.response?.data?.code === 404) {
      notifyWarning(errorMessage);
    } else {
      notifyError(errorMessage);
    }
  }

  function handleOption() {
    isRegistering.value = !isRegistering.value;
    username.value = '';
    password.value = '';
    submitted.value = false;
  }

  function togglePasswordVisibility() {
    showPassword.value = !showPassword.value;
  }

  function sleep(ms: number): Promise<void> {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

  return {
    username,
    password,
    submitted,
    loading,
    handleLogin,
    handleRegister,
    handleOption,
    showPassword,
    togglePasswordVisibility,
    isRegistering
  };
}
