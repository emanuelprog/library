import { describe, it, expect, vi, beforeEach } from 'vitest';
import type { Mock } from 'vitest';
import { useGenrePage } from './GenrePage';

import { createGenre, listAllGenre, removeGenre, updateGenre } from 'src/services/genreService';

vi.mock('src/services/genreService', () => ({
  listAllGenre: vi.fn(),
  createGenre: vi.fn(),
  updateGenre: vi.fn(),
  removeGenre: vi.fn(),
}));

vi.mock('src/services/messageService', () => ({
  notifyConfirm: vi.fn().mockResolvedValue(true),
  notifyError: vi.fn(),
  notifySuccess: vi.fn(),
  notifyWarning: vi.fn(),
}));

describe('useGenrePage', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('carrega gêneros com loadItems', async () => {
    const createdAt = new Date('2024-01-01');
    (listAllGenre as Mock).mockResolvedValue([{ id: '1', description: 'Ficção', createdAt }]);
    const { loadItems, items } = useGenrePage();
    await loadItems();
    expect(items.value.length).toBe(1);
    expect(items.value[0]?.description).toBe('Ficção');
  });

  it('cria gênero com onSubmit', async () => {
    const newGenre = { id: null, description: 'Novo Gênero', createdAt: null };
    (createGenre as Mock).mockResolvedValue({ ...newGenre, id: '2', createdAt: new Date() });
    const { form, submitAndClose } = useGenrePage();
    form.value = { ...newGenre };
    await submitAndClose();
    expect(createGenre).toHaveBeenCalledWith(newGenre);
  });

  it('atualiza gênero com onSubmit', async () => {
    const updatedGenre = { id: '1', description: 'Gênero Atualizado', createdAt: new Date() };
    (updateGenre as Mock).mockResolvedValue(updatedGenre);
    const { form, submitAndClose } = useGenrePage();
    form.value = { ...updatedGenre };
    await submitAndClose();
    expect(updateGenre).toHaveBeenCalledWith('1', updatedGenre);
  });

  it('remove gênero com deleteItem', async () => {
    (removeGenre as Mock).mockResolvedValue({});
    (listAllGenre as Mock).mockResolvedValue([]);
    const { items, deleteItem, loadItems } = useGenrePage();
    items.value = [{ id: '1', description: 'Ficção', createdAt: new Date() }];
    await deleteItem(0);
    expect(removeGenre).toHaveBeenCalledWith('1');
    await loadItems();
  });
});
