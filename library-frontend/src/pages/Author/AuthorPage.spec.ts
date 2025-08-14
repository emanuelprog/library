import { describe, it, expect, vi, beforeEach } from 'vitest';
import type { Mock } from 'vitest';
import { useAuthorPage } from './AuthorPage';

import { listAllAuthor, createAuthor, updateAuthor, removeAuthor } from 'src/services/authorService';

vi.mock('src/services/authorService', () => ({
  listAllAuthor: vi.fn(),
  createAuthor: vi.fn(),
  updateAuthor: vi.fn(),
  removeAuthor: vi.fn()
}));

vi.mock('src/services/messageService', () => ({
  notifyConfirm: vi.fn().mockResolvedValue(true),
  notifyError: vi.fn(),
  notifySuccess: vi.fn(),
  notifyWarning: vi.fn(),
}));

describe('useAuthorPage', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('carrega autores com loadItems', async () => {
    (listAllAuthor as Mock).mockResolvedValue([{ id: 1, name: 'Autor 1', createdAt: '2024-01-01' }]);
    const { loadItems, items } = useAuthorPage();
    await loadItems();
    expect(items.value.length).toBe(1);
    expect(items.value[0]?.name).toBe('Autor 1');
  });

  it('cria autor com onSubmit', async () => {
    (createAuthor as Mock).mockResolvedValue({ id: 2, name: 'Novo Autor' });
    const { form, submitAndClose } = useAuthorPage();
    form.value.name = 'Novo Autor';
    await submitAndClose();
    expect(createAuthor).toHaveBeenCalled();
  });

  it('atualiza autor com onSubmit', async () => {
    (updateAuthor as Mock).mockResolvedValue({ id: "1", name: "Autor Atualizado" });
    const { form, submitAndClose } = useAuthorPage();
    form.value.id = "1";
    form.value.name = "Autor Atualizado";
    await submitAndClose();
    expect(updateAuthor).toHaveBeenCalledWith("1", { id: "1", name: "Autor Atualizado", createdAt: null });
  });

  it('remove autor com deleteItem', async () => {
  (removeAuthor as Mock).mockResolvedValue({});
  (listAllAuthor as Mock).mockResolvedValue([]);
  const { items, deleteItem, loadItems } = useAuthorPage();
  items.value = [{ id: '1', name: 'Autor 1', createdAt: null }];
  await deleteItem(0);
  expect(removeAuthor).toHaveBeenCalledWith('1');
  await loadItems();
});
});
