const test = require('node:test');
const assert = require('node:assert/strict');

const { toAiReference } = require('../out/reference');

test('adds @ prefix to a relative path', () => {
  assert.equal(toAiReference('src/components/Button.tsx'), '@src/components/Button.tsx');
});

test('normalizes Windows separators', () => {
  assert.equal(toAiReference('src\\components\\Button.tsx'), '@src/components/Button.tsx');
});

test('returns undefined for an empty path', () => {
  assert.equal(toAiReference(''), undefined);
});
