import { describe, it, expect, vi, beforeEach } from 'vitest';
import type { Mock } from 'vitest';
import { useLoginPage } from './LoginPage';

import { login, register } from 'src/services/authService';
import { notifyError, notifySuccess } from 'src/services/messageService';

const setSelectedUserMock = vi.fn();
const pushMock = vi.fn().mockResolvedValue(undefined);

vi.mock('src/services/authService', () => ({
  login: vi.fn(),
  register: vi.fn(),
}));

vi.mock('src/services/messageService', () => ({
  notifyError: vi.fn(),
  notifyWarning: vi.fn(),
  notifySuccess: vi.fn(),
}));

vi.mock('src/stores/userStore', () => ({
  useUserStore: vi.fn(() => ({
    setSelectedUser: setSelectedUserMock,
  })),
}));

vi.mock('vue-router', () => ({
  useRouter: vi.fn(() => ({
    push: pushMock,
  })),
}));

describe('useLoginPage', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('lida com login bem-sucedido', async () => {
    const mockUser = { id: '1', username: 'testuser' };
    (login as Mock).mockResolvedValue(mockUser);

    const { username, password, handleLogin, loading, submitted } = useLoginPage();
    username.value = 'testuser';
    password.value = 'password123';

    await handleLogin();

    expect(login).toHaveBeenCalledWith('testuser', 'password123');
    expect(setSelectedUserMock).toHaveBeenCalledWith(mockUser);
    expect(pushMock).toHaveBeenCalledWith('/home');
    expect(loading.value).toBe(false);
    expect(submitted.value).toBe(false);
  });

  it('lida com login com campos vazios', async () => {
    const { handleLogin, submitted, loading } = useLoginPage();

    await handleLogin();

    expect(login).not.toHaveBeenCalled();
    expect(loading.value).toBe(false);
    expect(submitted.value).toBe(true);
  });

  it('lida com erro no login', async () => {
    (login as Mock).mockRejectedValue(new Error('Falha na autenticação'));

    const { username, password, handleLogin, loading, submitted } = useLoginPage();
    username.value = 'testuser';
    password.value = 'password123';

    await handleLogin();

    expect(login).toHaveBeenCalledWith('testuser', 'password123');
    expect(notifyError).toHaveBeenCalledWith('Erro ao autenticar.');
    expect(loading.value).toBe(false);
    expect(submitted.value).toBe(false);
  });

  it('lida com registro bem-sucedido', async () => {
    (register as Mock).mockResolvedValue(undefined);

    const { username, password, handleRegister, isRegistering, loading, submitted } = useLoginPage();
    username.value = 'newuser';
    password.value = 'password123';
    isRegistering.value = true;

    await handleRegister();

    expect(register).toHaveBeenCalledWith('newuser', 'password123');
    expect(notifySuccess).toHaveBeenCalledWith('Usuário registrado com sucesso!');
    expect(isRegistering.value).toBe(false);
    expect(username.value).toBe('');
    expect(password.value).toBe('');
    expect(loading.value).toBe(false);
    expect(submitted.value).toBe(false);
  });

  it('lida com registro com campos vazios', async () => {
    const { handleRegister, submitted, loading, isRegistering } = useLoginPage();
    isRegistering.value = true;

    await handleRegister();

    expect(register).not.toHaveBeenCalled();
    expect(loading.value).toBe(false);
    expect(submitted.value).toBe(true);
    expect(isRegistering.value).toBe(true);
  });

  it('lida com erro no registro', async () => {
    (register as Mock).mockRejectedValue(new Error('Falha no registro'));

    const { username, password, handleRegister, isRegistering, loading, submitted } = useLoginPage();
    username.value = 'newuser';
    password.value = 'password123';
    isRegistering.value = true;

    await handleRegister();

    expect(register).toHaveBeenCalledWith('newuser', 'password123');
    expect(notifyError).toHaveBeenCalledWith('Erro ao registrar usuário.');
    expect(loading.value).toBe(false);
    expect(submitted.value).toBe(false);
    expect(isRegistering.value).toBe(true);
  });

  it('lida com alternância de opção (register/login)', () => {
    const { handleOption, username, password, submitted, isRegistering } = useLoginPage();
    isRegistering.value = false;
    username.value = 'testuser';
    password.value = 'password123';
    submitted.value = true;

    handleOption();

    expect(isRegistering.value).toBe(true);
    expect(username.value).toBe('');
    expect(password.value).toBe('');
    expect(submitted.value).toBe(false);
  });

  it('lida com visibilidade da senha', () => {
    const { togglePasswordVisibility, showPassword } = useLoginPage();
    showPassword.value = false;

    togglePasswordVisibility();
    expect(showPassword.value).toBe(true);

    togglePasswordVisibility();
    expect(showPassword.value).toBe(false);
  });
});
