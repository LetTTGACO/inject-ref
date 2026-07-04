const test = require('node:test');
const assert = require('node:assert/strict');

const { toAiReference, toSingleAiReference } = require('../out/reference');

test('adds @ prefix to a relative path', () => {
  assert.equal(toAiReference('src/components/Button.tsx'), '@src/components/Button.tsx');
});

test('normalizes Windows separators', () => {
  assert.equal(toAiReference('src\\components\\Button.tsx'), '@src/components/Button.tsx');
});

test('returns undefined for an empty path', () => {
  assert.equal(toAiReference(''), undefined);
});

test('adds @ prefix to a single copied relative path', () => {
  assert.equal(toSingleAiReference('src/components/Button.tsx'), '@src/components/Button.tsx');
});

test('rejects multiple copied relative paths', () => {
  assert.equal(toSingleAiReference('src/Button.tsx\nsrc/Input.tsx'), undefined);
});
